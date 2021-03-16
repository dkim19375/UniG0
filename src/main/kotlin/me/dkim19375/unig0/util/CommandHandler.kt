package me.dkim19375.unig0.util

import me.dkim19375.dkim19375jdautils.holders.MessageRecievedHolder
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.properties.ServerProperties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*

open class CommandHandler(private val JDA: JDA) : ListenerAdapter() {
    private fun getMessage(message: String, serverId: String?): MessageRecievedHolder? {
        if (serverId == null) {
            if (!message.startsWith(JDA.selfUser.asMention.replaceFirst("@".toRegex(), "@!"))) {
                return null
            }
        }
        val prefix: String = if (serverId == null) JDA.selfUser.asMention.replaceFirst("@".toRegex(), "@!")
        else if (message.startsWith(JDA.selfUser.asMention.replaceFirst("@".toRegex(), "@!"))) {
            JDA.selfUser.asMention.replaceFirst("@".toRegex(), "@!")
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
            onMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (FileUtils.getDisabledChannels(event.guild.id).contains(event.channel.id)) {
            return
        }
        val msg = getMessage(event.message.contentRaw, event.guild.id)
        if (msg != null) {
            onGuildMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
        }
    }

    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        val msg = getMessage(event.message.contentRaw, null)
        if (msg != null) {
            onPrivateMessageReceived(msg.command, msg.args, msg.prefix, msg.all, event)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
    fun onMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: MessageReceivedEvent
    ) {
    }

    @Suppress("UNUSED_PARAMETER")
    open fun onGuildMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun onPrivateMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: PrivateMessageReceivedEvent
    ) {
    }
}