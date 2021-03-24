package me.dkim19375.unig0.event

import me.dkim19375.dkim19375jdautils.holders.MessageRecievedHolder
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.property.ServerProperties
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.ArrayList

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
        val argsList: MutableList<String> = ArrayList()
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
        val msg = getMessage(event.message.contentRaw, event.guild.id)
        if (msg != null) {
            try {
                main.onMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
            } catch (e: Exception) {
                e.printStackTrace()
                event.channel.sendMessage("An internal error has occurred!").queue()
            }
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (FileUtils.getDisabledChannels(event.guild.id).contains(event.channel.id)) {
            return
        }
        val msg = getMessage(event.message.contentRaw, event.guild.id)
        if (msg != null) {
            try {
                main.onGuildMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
            } catch (e: Exception) {
                e.printStackTrace()
                event.channel.sendMessage("An internal error has occurred!").queue()
            }
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        val msg = getMessage(event.message.contentRaw, null)
        if (msg != null) {
            try {
                main.onPrivateMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
            } catch (e: Exception) {
                e.printStackTrace()
                event.channel.sendMessage("An internal error has occurred!").queue()
            }
        }
    }
}