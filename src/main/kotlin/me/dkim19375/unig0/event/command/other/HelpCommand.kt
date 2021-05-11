package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.function.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class HelpCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "help"
    override val name = "Help"
    override val aliases = setOf<String>()
    override val description = "See the bot's commands"
    override val arguments = setOf(
        CommandArg(this, "fun", "View commands in the options category"),
        CommandArg(this, "levels", "View commands in the options category"),
        CommandArg(this, "logging", "View commands in the logging category"),
        CommandArg(this, "roles", "View commands in the roles category"),
        CommandArg(this, "tags", "View commands in the tags category"),
        CommandArg(this, "utilities", "View commands in the utilities category"),
        CommandArg(this, "other", "View commands in the other category"),
        CommandArg(this, "<command>", "View commands in the other category")
    )
    override val type = CommandType.OTHER
    override val minArgs = 1

    override fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        val type = CommandType.getByName(args[0])
        if (type == null) {
            val command = main.commands.getCommandByName(args[0])
            if (command == null) {
                sendHelpUsage(cmd, event)
                return
            }
            sendHelpUsage(cmd, event, command)
            return
        }
        args[0].getCommand()?.let { helpCmd ->
            
        }
        val embedManager = EmbedManager("UniG0 $name: ${type.displayname}", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField("TIP:", "Do ${FileUtils.getPrefix(event.guild.id)}help <command> " +
                "to view information about a specific command!", false)
        embedManager.embedBuilder.addField("Information:", "Commands in the ${type.displayname} category", false)
        embedManager.embedBuilder.addField(main.commands.getOfType(type).combinedCmds().getEmbedField("Commands - ${type.displayname}:"))
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }
}