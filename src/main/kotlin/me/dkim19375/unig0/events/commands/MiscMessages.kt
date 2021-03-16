package me.dkim19375.unig0.events.commands

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.util.CommandHandler
import me.dkim19375.unig0.util.FileUtils
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color
import java.util.*

class MiscMessages(private val jda: JDA) : CommandHandler(jda) {
    override fun onGuildMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        if (event.author.isBot) {
            return
        }
        val commands: MutableSet<String> = HashSet()
        if (FileUtils.getDeletedCommands(event.guild.id).contains(cmd)) {
            event.message.delete().queue()
        }
        if (FileUtils.getDeletedCommands(event.guild.id).contains("*")) {
            event.message.delete().queue()
        }
        commands.add("help")
        commands.add("options")
        commands.add("ping")
        commands.add("announce <channel> <message>")
        commands.add("embed")
        when (cmd.toLowerCase()) {
            "help" -> {
                val embedManager = EmbedManager("UniG0 Help", Color.BLUE, cmd, event.author)
                embedManager.embedBuilder.addField(
                    EmbedUtils.getEmbedGroup(
                        EntryImpl<String, Set<String>>(
                            "Commands",
                            commands
                        )
                    )
                )
                event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
                return
            }
            "ping" -> {
                jda.restPing.queue { time: Long ->
                    val embedManagerPing = EmbedManager("UniG0 Pinger", Color.GREEN, cmd, event.author)
                    embedManagerPing.embedBuilder.setDescription("**Pong!**\nTook **$time ms**")
                    event.channel.sendMessage(embedManagerPing.embedBuilder.build()).queue()
                }
                return
            }
            "announce" -> {
                if (args.size < 2) {
                    val embedManagerAnnounce = EmbedManager("UniG0 Help", Color.RED, cmd, event.author)
                    embedManagerAnnounce.embedBuilder.addField(
                        MessageEmbed.Field(
                            "Announcements Command Format",
                            "announce <channel> <message>", true
                        )
                    )
                    event.channel.sendMessage(embedManagerAnnounce.embedBuilder.build()).queue()
                    return
                }
                val message = getRestArgs(args, 1)
                if (getChannel(args[0]) == null) {
                    val embedManagerAnnounce = EmbedManager("Invalid Syntax", Color.RED, cmd, event.author)
                    embedManagerAnnounce.embedBuilder.addField(
                        MessageEmbed.Field(
                            "Announcements Command Format",
                            "announce <channel> <message>", true
                        )
                    )
                    event.channel.sendMessage(embedManagerAnnounce.embedBuilder.build()).queue()
                    return
                }
                val embedManagerAnnounce = EmbedManager(
                    "Announcement", Color.YELLOW, "Sent by "
                            + event.author.name + "#" + event.author.discriminator, null
                )
                embedManagerAnnounce.embedBuilder.setDescription(message)
                getChannel(args[0])!!.sendMessage(embedManagerAnnounce.embedBuilder.build()).queue()
                return
            }
        }
    }

    fun getChannel(ids: String): TextChannel? {
        var newString = ids
        newString = newString.replace("<", "")
        newString = newString.replace(">", "")
        newString = newString.replace("#", "")
        return jda.getTextChannelById(newString)
    }

    fun getRestArgs(args: Array<String>, index: Int): String {
        var str = StringBuilder()
        var i = 0
        for (ignored in args) {
            if (i < index) {
                i++
                continue
            }
            str.append(args[i]).append(" ")
            i++
        }
        str = StringBuilder(str.toString().trim { it <= ' ' })
        return str.toString()
    }
}