package me.dkim19375.unig0.event.command.utilities

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

private const val DKIM19375_ID = 521485088995672084
class RestartCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "restart"
    override val name = "Restart"
    override val aliases = setOf<String>()
    override val description = "Restart the bot"
    override val arguments = setOf<CommandArg>()
    override val type = CommandType.OTHER
    override val minArgs = 0
    private val jda = main.jda

    override fun onCommand(cmd: String, args: Array<String>, prefix: String, all: String, event: MessageReceivedEvent) {
        if (event.author.idLong != DKIM19375_ID) {
            event.channel.sendMessage("You do not have permission!").queue()
            return
        }
        event.channel.sendMessage("Restarting bot...").queue()
    }
}