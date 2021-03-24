package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.*
import me.dkim19375.unig0.util.function.*
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class HelpCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "help"
    override val name = "Help"
    override val aliases = setOf<String>()
    override val description = "See the bot's commands"
    override val arguments = setOf(
        CommandArg(this, "options", "View commands in the options category"),
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
        val type = CommandType.getByName(args[0])
        if (type == null) {
            val command = main.commands.getCommandByName(args[0])
            if (command != null) {
                val embedManager = EmbedManager("UniG0 $name: ${command.name}", Color.BLUE, cmd, event.author)
                embedManager.embedBuilder.addField("Information:", command.description, false)
                embedManager.embedBuilder.addField("Aliases:", command.aliases.putBetween(", "), false)
                if (command.arguments.isEmpty()) {
                    embedManager.embedBuilder.addField("Arguments - ${command.name}:", "None", false)
                } else {
                    embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Arguments: ${command.name}",
                        command.arguments.combinedArgs())))
                }
                event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
                return
            }
            val embedManager = EmbedManager("UniG0 $name", Color.BLUE, cmd, event.author)
            embedManager.embedBuilder.addField("Information:", description, false)
            embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Arguments:", arguments.combinedArgs())))
            event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
            return
        }
        val embedManager = EmbedManager("UniG0 $name: ${type.displayname}", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField("TIP:", "Do ${FileUtils.getPrefix(event.guild.id)}help <command> " +
                "to view information about a specific command!", false)
        embedManager.embedBuilder.addField("Information:", "Commands in the ${type.displayname} category", false)
        if (main.commands.getOfType(type).isEmpty()) {
            embedManager.embedBuilder.addField("Commands - ${type.displayname}:", "None", false)
        } else {
            embedManager.embedBuilder.addField(EmbedUtils.getEmbedGroup(EntryImpl("Commands - ${type.displayname}:",
                main.commands.getOfType(type).combinedCmds())))
        }
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }
}