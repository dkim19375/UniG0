package me.dkim19375.unig0.event

import me.dkim19375.dkim19375jdautils.holders.MessageRecievedHolder
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.function.containsIgnoreCase
import me.dkim19375.unig0.util.property.ServerProperties
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener(private val main: UniG0) : ListenerAdapter() {
    private fun getMessage(message: String, serverId: String?): MessageRecievedHolder? {
        if (serverId == null) {
            if (!message.startsWith(main.jda.selfUser.asMention.replaceFirst("@".toRegex(), "@!"))) {
                return null
            }
        }
        val prefix: String = if (serverId == null) main.jda.selfUser.asMention.replaceFirst("@".toRegex(), "@!")
        else if (message.startsWith(main.jda.selfUser.asMention.replaceFirst("@".toRegex(), "@!"))) {
            main.jda.selfUser.asMention.replaceFirst("@".toRegex(), "@!")
        } else {
            if (message.toLowerCase().startsWith(
                    UniG0.fileManager.getServerConfig(serverId).get(ServerProperties.prefix).toLowerCase()
                )
            ) {
                UniG0.fileManager.getServerConfig(serverId).get(ServerProperties.prefix)
            } else {
                return null
            }
        }
        if (message.length <= prefix.length) {
            return null
        }
        var command = message
        command = command.substring(prefix.length)
        command = command.trim { it <= ' ' }
        val allArray = command.split(" ").toTypedArray()
        command = command.split(" ").toTypedArray()[0]
        val argsList = mutableListOf<String>()
        var first = true
        for (s in allArray) {
            if (!first) {
                argsList.add(s)
            }
            first = false
        }
        val args = argsList.toTypedArray()
        return MessageRecievedHolder(command, args, prefix, message)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        try {
            if (FileUtils.getDisabledChannels(event.guild.id).contains(event.channel.id)) {
                return
            }
        } catch (ignored: IllegalStateException) {
        }
        try {
            main.sendEvent { c -> c.onMessageReceived(event.message.contentRaw, event) }
        } catch (e: Exception) {
            e.printStackTrace()
            event.channel.sendMessage("An internal error has occurred!").queue()
        }
        val msg = getMessage(
            event.message.contentRaw, try {
                event.guild
            } catch (_: IllegalStateException) {
                null
            }?.id
        )
        if (msg != null) {
            main.sendEvent { c ->
                if (!isValid(c, msg.command, msg.args.toList(), event)) {
                    return@sendEvent
                }
                try {
                    println("sent command - ${c.command}")
                    c.onCommand(msg.command, msg.args.toList(), msg.prefix, msg.all, event)
                } catch (e: Exception) {
                    e.printStackTrace()
                    event.channel.sendMessage("An internal error has occurred!").queue()
                }
            }
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (FileUtils.getDisabledChannels(event.guild.id).contains(event.channel.id)) {
            return
        }
        try {
            main.sendEvent { c -> c.onGuildMessageReceived(event.message.contentRaw, event) }
        } catch (e: Exception) {
            e.printStackTrace()
            event.channel.sendMessage("An internal error has occurred!").queue()
        }
        val msg = getMessage(event.message.contentRaw, event.guild.id)
        if (msg != null) {
            main.sendEvent { c ->
                if (!isValid(c, msg.command, msg.args.toList(), event)) {
                    return@sendEvent
                }
                try {
                    println("sent guild command - ${c.command}")
                    c.onGuildCommand(msg.command, msg.args.toList(), msg.prefix, msg.all, event)
                } catch (e: Exception) {
                    e.printStackTrace()
                    event.channel.sendMessage("An internal error has occurred!").queue()
                }
            }
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        try {
            main.sendEvent { c -> c.onPrivateMessageReceived(event.message.contentRaw, event) }
        } catch (e: Exception) {
            e.printStackTrace()
            event.channel.sendMessage("An internal error has occurred!").queue()
        }
        val msg = getMessage(event.message.contentRaw, null)
        if (msg != null) {
            try {
                main.sendEvent { c ->
                    if (!isValid(c, msg.command, msg.args.toList(), event)) {
                        return@sendEvent
                    }
                    try {
                        c.onPrivateCommand(msg.command, msg.args.toList(), msg.prefix, msg.all, event)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        event.channel.sendMessage("An internal error has occurred!").queue()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                event.channel.sendMessage("An internal error has occurred!").queue()
            }
        }
    }

    private fun isValid(
        command: Command,
        cmd: String,
        args: List<String>,
        event: GuildMessageReceivedEvent
    ): Boolean =
        isValid(command, cmd, args, event.member, event.author, event.guild, event.message, event.channel, event)

    private fun isValid(
        command: Command,
        cmd: String,
        args: List<String>,
        member: Member?,
        user: User,
        guild: Guild?,
        message: Message,
        channel: MessageChannel,
        event: Event
    ): Boolean {
        if (!cmd.equals(command.command, ignoreCase = true)) {
            return false
        }
        if (user.isBot) {
            return false
        }
        if (guild != null && FileUtils.getDeletedCommands(guild.id).containsIgnoreCase(cmd)) {
            message.delete().queue()
        }
        if (guild != null && FileUtils.getDeletedCommands(guild.id).contains("*")) {
            message.delete().queue()
        }
        member?.let {
            if (channel is GuildChannel) {
                if (member.hasPermission(channel, command.permissions)) {
                    return@let
                }
            } else if (member.hasPermission(command.permissions)) {
                return@let
            }
            channel.sendMessage(
                "You do not have permission! (Required permission${if (command.permissions.size <= 1) "" else "s"}: ${
                    command.permissions.joinToString(
                        ", ", transform = Permission::getName
                    )
                })"
            ).queue()
            return false
        }
        if (args.size < command.minArgs) {
            when (event) {
                is MessageReceivedEvent -> command.sendHelpUsage(cmd, event)
                is PrivateMessageReceivedEvent -> command.sendHelpUsage(cmd, event)
                is GuildMessageReceivedEvent -> command.sendHelpUsage(cmd, event)
            }
            return false
        }
        return true
    }

    private fun isValid(
        command: Command,
        cmd: String,
        args: List<String>,
        event: MessageReceivedEvent
    ): Boolean = isValid(
        command, cmd, args, event.member, event.author, try {
            event.guild
        } catch (_: IllegalStateException) {
            null
        }, event.message, event.channel, event
    )

    private fun isValid(
        command: Command,
        cmd: String,
        args: List<String>,
        event: PrivateMessageReceivedEvent
    ): Boolean = isValid(command, cmd, args, null, event.author, null, event.message, event.channel, event)
}