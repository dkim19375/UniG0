package me.dkim19375.unig0.util

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.util.function.combinedArgs
import me.dkim19375.unig0.util.function.containsIgnoreCase
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import java.awt.Color

abstract class Command(jda: JDA) {
    abstract val command: String
    abstract val name: String
    abstract val aliases: Set<String>
    abstract val description: String
    abstract val arguments: Set<CommandArg>
    abstract val type: CommandType
    abstract val minArgs: Int
    open val permissions: Set<Permission> = setOf()

    fun sendHelpUsage(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManager = EmbedManager("UniG0 $name", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Arguments:", arguments.combinedArgs())))
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }

    fun sendHelpUsage(
        cmd: String,
        event: MessageReceivedEvent
    ) {
        val embedManager = EmbedManager("UniG0 $name", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Arguments:", arguments.combinedArgs())))
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }

    fun sendHelpUsage(
        cmd: String,
        event: PrivateMessageReceivedEvent
    ) {
        val embedManager = EmbedManager("UniG0 $name", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Arguments:", arguments.combinedArgs())))
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }

    open fun onMessageReceived(
        message: String,
        event: MessageReceivedEvent
    ) {
    }

    open fun onCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: MessageReceivedEvent
    ) {
    }

    open fun onGuildMessageReceived(
        message: String,
        event: GuildMessageReceivedEvent
    ) {
    }

    open fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
    }

    open fun onPrivateMessageReceived(
        message: String,
        event: PrivateMessageReceivedEvent
    ) {
    }

    open fun onPrivateCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: PrivateMessageReceivedEvent
    ) {
    }
}