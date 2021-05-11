package me.dkim19375.unig0.event.command.other

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.system.exitProcess

private const val DKIM19375_ID = 521485088995672084
class StopCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "stop"
    override val name = "Stop"
    override val aliases = setOf<String>()
    override val description = "Stop the bot"
    override val arguments = setOf<CommandArg>()
    override val type = CommandType.OTHER
    override val minArgs = 0
    private val jda = main.jda

    override fun onCommand(cmd: String, args: List<String>, prefix: String, all: String, event: MessageReceivedEvent) {
        if (event.author.idLong != DKIM19375_ID) {
            event.channel.sendMessage("You do not have permission!").queue()
            return
        }
        event.channel.sendMessage("Stopping bot...").queue({
            main.stopped = true
            main.jda.shutdown()
            exitProcess(0)
        }
        ) { error ->
            event.channel.sendMessage("Error!").queue()
            throw IllegalStateException("Could not stop bot!", error)
        }
    }
}