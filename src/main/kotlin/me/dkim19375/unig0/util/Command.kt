package me.dkim19375.unig0.util

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.function.combinedArgs
import me.dkim19375.unig0.util.function.getEmbedField
import me.dkim19375.unig0.util.property.ServerProperties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import java.awt.Color

abstract class Command(private val jda: JDA) {
    abstract val command: String
    abstract val name: String
    abstract val aliases: Set<String>
    abstract val description: String
    abstract val arguments: Set<CommandArg>
    abstract val type: CommandType
    abstract val minArgs: Int
    open val permissions: Set<Permission> = setOf()
    open val whitelistUsers: Set<Long> = setOf()

    fun sendHelpUsage(
        cmd: String,
        event: Event,
        command: Command = this
    ) {
        val user = when (event) {
            is GuildMessageReceivedEvent -> event.author
            is PrivateMessageReceivedEvent -> event.author
            is MessageReceivedEvent -> event.author
            else -> return
        }
        val member: Member? = (event as? GuildMessageReceivedEvent)?.member
        val guild = when (event) {
            is GuildMessageReceivedEvent -> event.guild
            is MessageReceivedEvent -> event.guild
            else -> return
        }
        val channel = when (event) {
            is GuildMessageReceivedEvent -> event.channel
            is PrivateMessageReceivedEvent -> event.channel
            is MessageReceivedEvent -> event.channel
            else -> return
        }
        if (!hasPermissions(user, member, channel as? GuildChannel)) {
            return
        }
        val embedManager = EmbedManager("UniG0 ${command.name}", Color.BLUE, cmd, user)
        embedManager.embedBuilder.addField(
            "Information:",
            command.description.plus(
                "\n**Prefix: ${
                    UniG0.fileManager.getServerConfig(guild.id).get(ServerProperties.prefix)
                }**"
            ), false
        )
        embedManager.embedBuilder.addField(command.aliases.getEmbedField("Aliases:"))
        embedManager.embedBuilder.addField(command.arguments.combinedArgs().getEmbedField("Arguments:"))
        channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }
    
    fun hasPermissions(user: User, member: Member? = null, channel: GuildChannel? = null): Boolean {
        if (whitelistUsers.isNotEmpty() && !whitelistUsers.contains(user.idLong)) {
            return false
        }
        member ?: return true
        if (channel != null) {
            return member.hasPermission(channel, permissions)
        }
        return member.hasPermission(permissions)
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