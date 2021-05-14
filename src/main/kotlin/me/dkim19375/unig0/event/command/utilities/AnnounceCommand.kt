package me.dkim19375.unig0.event.command.utilities

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.dkim19375jdautils.embed.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import me.dkim19375.unig0.util.function.getChannel
import me.dkim19375.unig0.util.function.getRestArgs
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class AnnounceCommand(private val main: UniG0) : Command(main) {
    override val command = "announce"
    override val name = "Announcements"
    override val aliases = setOf<String>()
    override val description = "Announce a message"
    override val arguments = setOf(
        CommandArg(this, "<channel> <message>", "The message to announce")
    )
    override val type = CommandTypes.UTILITIES
    override val minArgs = 2
    override val permissions = setOf(Permission.MESSAGE_MANAGE)
    private val jda = main.jda

    override fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        val message = args.getRestArgs(1)
        val channel = args[0].getChannel(jda)
        if (channel == null) {
            val embedManagerAnnounce = EmbedManager("Invalid Syntax", Color.RED, cmd, event.author)
            embedManagerAnnounce.embedBuilder.addField(
                MessageEmbed.Field(
                    "Announcements Command Format",
                    "announce <channel> <message>", false
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
        channel.sendMessage(embedManagerAnnounce.embedBuilder.build()).queue()
        return
    }
}