package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class PingCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "ping"
    override val name = "Ping"
    override val aliases = setOf<String>()
    override val description = "See the bot's latency"
    override val arguments = setOf<CommandArg>()
    override val type = CommandType.OTHER
    override val minArgs = 0
    private val jda = main.jda

    override fun onGuildMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        if (!isValid(cmd, args, event)) {
            return
        }
        jda.restPing.queue { time: Long ->
            val embedManagerPing = EmbedManager("UniG0 Pinger", Color.GREEN, cmd, event.author)
            embedManagerPing.embedBuilder.setDescription("**Pong!**\nTook **$time ms**")
            event.channel.sendMessage(embedManagerPing.embedBuilder.build()).queue()
        }
    }
}