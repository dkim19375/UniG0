package me.dkim19375.unig0.event.command.utilities

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.function.getChannel
import me.dkim19375.unig0.util.function.putBetween
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class OptionsCommand(private val main: UniG0) : Command(main.jda) {
    override val command = "options"
    override val name = "Options"
    override val aliases = setOf<String>()
    override val description = "Change options for the bot"
    override val permissions = setOf(Permission.MANAGE_SERVER)
    override val arguments = setOf(
        CommandArg(this, "prefix", "Change the bot prefix"),
        CommandArg(this, "reload", "Reload the bot's data and configuration files"),
        CommandArg(this, "delete-commands *|<command>", "Change which commands get deleted when used"),
        CommandArg(this, "disable-channels <channel|id>", "Change which channels are ignored"),
        CommandArg(
            this,
            "welcomer WelcomeMessage <message>|DMMessage <message>|MessageEnabled <true/false>|DMEnabled <true/false>|channel <channel>",
            "Change welcomer settings"
        ),
    )
    override val type = CommandType.UTILITIES
    override val minArgs = 1
    private val jda = main.jda

    override fun onGuildCommand(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        when (args[0].toLowerCase()) {
            "prefix" -> {
                if (args.size < 2) {
                    val embedManagerPrefix = EmbedManager("UniG0 Prefix", Color.BLUE, cmd, event.author)
                    embedManagerPrefix.embedBuilder.addField(
                        "Current Prefix:", """`${FileUtils.getPrefix(event.guild.id)}`
You can also use ${jda.selfUser.asMention} as the prefix!""", false
                    )
                    event.channel.sendMessage(embedManagerPrefix.embedBuilder.build()).queue()
                    return
                }
                FileUtils.setPrefix(event.guild.id, args[1])
                event.channel.sendMessage("Successfully changed the prefix to `" + FileUtils.getPrefix(event.guild.id) + "`!")
                    .queue()
                return
            }
            "reload" -> {
                FileUtils.reload()
                event.channel.sendMessage("Successfully reloaded files!").queue()
                return
            }
            "disable-channels" -> {
                if (args.size < 2) {
                    sendDisabledChannels(cmd, event)
                    return
                }
                val channel = args[1].getChannel(jda)
                if (channel == null) {
                    event.channel.sendMessage("Invalid channel!").queue()
                    return
                }
                if (FileUtils.getDisabledChannels(event.guild.id).contains(channel.id)) {
                    FileUtils.removeDisabledChannel(event.guild.id, channel.id)
                    event.channel.sendMessage("Successfully enabled the channel " + channel.asMention + "!")
                        .queue()
                    val embedManagerPrefix = EmbedManager("UniG0 Enabled Channels", Color.ORANGE, cmd, event.author)
                    embedManagerPrefix.embedBuilder.addField(
                        "Channels that are ignored:", """
     ${collectionNewLineChannels(FileUtils.getDisabledChannels(event.guild.id))}
     
     """.trimIndent(), false
                    )
                    event.channel.sendMessage(embedManagerPrefix.embedBuilder.build()).queue()
                    return
                }
                FileUtils.addDisabledChannel(event.guild.id, channel.id)
                event.channel.sendMessage("Successfully disabled the channel " + channel.asMention + "!")
                    .queue()
                sendDisabledChannels(cmd, event)
                return
            }
            "delete-commands" -> {
                if (args.size < 2) {
                    val embedManagerCMDDeletion =
                        EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.author)
                    embedManagerCMDDeletion.embedBuilder.addField(
                        "Commands that auto-delete:", """
     ${FileUtils.getDeletedCommands(event.guild.id).putBetween("\n")}
     
     """.trimIndent(), false
                    )
                    event.channel.sendMessage(embedManagerCMDDeletion.embedBuilder.build()).queue()
                    return
                }
                if (FileUtils.getDeletedCommands(event.guild.id).contains(args[1])) {
                    FileUtils.removeDeletedCommand(event.guild.id, args[1])
                    event.channel.sendMessage("Successfully removed the command `" + args[1] + "`!").queue()
                    sendAutoDeleteCmds(cmd, event)
                    return
                }
                FileUtils.addDeletedCommand(event.guild.id, args[1])
                event.channel.sendMessage("Successfully added the command `" + args[1] + "`!").queue()
                sendAutoDeleteCmds(cmd, event)
                return
            }
            "welcomer" -> {
                if (args.size >= 2) {
                    return
                }
                val embedManagerWelcomer = EmbedManager("UniG0 Welcomer", Color.BLUE, cmd, event.author)
                embedManagerWelcomer.embedBuilder.addField(
                    "Enabled:",
                    """
                                        ```
                                        Public Message: ${FileUtils.isWelcomeMessageEnabled(event.guild.id)}
                                        DM Message: ${FileUtils.isWelcomeDMEnabled(event.guild.id)}```
                                        """.trimIndent(), false
                )
                embedManagerWelcomer.embedBuilder.addField(
                    "Messages:",
                    """
                                        ```
                                        Public Message: ${FileUtils.getWelcomeMessage(event.guild.id)}
                                        DM Message: ${FileUtils.getDMMessage(event.guild.id)}```
                                        """.trimIndent(), false
                )
                if (FileUtils.getWelcomeChannel(event.guild.id).getChannel(jda) == null) {
                    embedManagerWelcomer.embedBuilder.addField(
                        "Channels:",
                        "Public Message: NONE", false
                    )
                } else {
                    embedManagerWelcomer.embedBuilder.addField(
                        "Channels:",
                        "Public Message: " + (FileUtils.getWelcomeChannel(event.guild.id).getChannel(jda)?.asMention
                            ?: "None"),
                        false
                    )
                }
                event.channel.sendMessage(embedManagerWelcomer.embedBuilder.build()).queue()
                return
            }
        }
    }

    private fun collectionNewLineChannels(collection: Collection<String>): String {
        val finished = StringBuilder()
        for (c in collection) {
            val s = c.getChannel(jda)?.asMention
            finished.append("\n").append(s)
        }
        return finished.toString()
    }

    private fun sendAutoDeleteCmds(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManagerCMDDeletion =
            EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.author)
        embedManagerCMDDeletion.embedBuilder.addField(
            "Commands that auto-delete:", """
     ```${FileUtils.getDeletedCommands(event.guild.id).putBetween("\n")}
     ```
     """.trimIndent(), false
        )
        event.channel.sendMessage(embedManagerCMDDeletion.embedBuilder.build()).queue()
    }


    private fun sendDisabledChannels(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManagerPrefix = EmbedManager("UniG0 Disabled Channels", Color.ORANGE, cmd, event.author)
        embedManagerPrefix.embedBuilder.addField(
            "Channels that are ignored:", """
     ${collectionNewLineChannels(FileUtils.getDisabledChannels(event.guild.id))}
     
     """.trimIndent(), false
        )
        event.channel.sendMessage(embedManagerPrefix.embedBuilder.build()).queue()
    }
}