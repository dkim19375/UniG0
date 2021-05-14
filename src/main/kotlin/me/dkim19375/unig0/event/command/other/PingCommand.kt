package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.dkim19375jdautils.embed.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class PingCommand(private val main: UniG0) : Command(main) {
    override val command = "ping"
    override val name = "Ping"
    override val aliases = setOf<String>()
    override val description = "See the bot's latency"
    override val arguments = setOf<CommandArg>()
    override val type = CommandTypes.OTHER
    override val minArgs = 0
    private val jda = main.jda

    override fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        jda.restPing.queue { time: Long ->
            val embedManagerPing = EmbedManager("UniG0 Pinger", Color.GREEN, cmd, event.author)
            embedManagerPing.embedBuilder.setDescription("**Pong!**\nTook **$time ms**")
            event.channel.sendMessage(embedManagerPing.embedBuilder.build()).queue()
        }
    }
}