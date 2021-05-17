package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import kotlin.system.exitProcess

class StopCommand(private val main: UniG0) : Command(main) {
    override val command = "stop"
    override val name = "Stop"
    override val aliases = setOf<String>()
    override val description = "Stop the bot"
    override val arguments = setOf<CommandArg>()
    override val type = CommandTypes.OTHER
    override val minArgs = 0
    override val whitelistUsers = setOf(521485088995672084)
    private val jda = main.jda

    override fun onCommand(cmd: String, args: List<String>, prefix: String, all: String, event: MessageReceivedEvent) {
        event.channel.sendMessage("Stopping bot...").queue({
            main.jda.shutdown()
            exitProcess(0)
        }
        ) { error ->
            event.channel.sendMessage("Error!").queue()
            throw IllegalStateException("Could not stop bot!", error)
        }
    }
}