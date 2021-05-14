package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.dkim19375jdautils.embed.EmbedManager
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.function.containsIgnoreCase
import me.dkim19375.unig0.util.function.getChannel
import me.dkim19375.unig0.util.function.getEmbedField
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class OptionsCommand(private val main: UniG0) : Command(main) {
    override val command = "options"
    override val name = "Options"
    override val aliases = setOf<String>()
    override val description = "Change options for the bot"
    override val permissions = setOf(Permission.ADMINISTRATOR)
    override val arguments = setOf(
        CommandArg(this, "prefix", "Change the bot prefix"),
        CommandArg(this, "reload", "Reload the bot's data and configuration files"),
        CommandArg(this, "delete-commands *|<command>", "Change which commands get deleted when used"),
        CommandArg(this, "disable-commands <command>", "Change which commands are disabled"),
        CommandArg(this, "disable-channels <channel|id>", "Change which channels are ignored"),
        CommandArg(
            this,
            "welcomer WelcomeMessage <message>|DMMessage <message>|MessageEnabled <true/false>|DMEnabled <true/false>|channel <channel>",
            "Change welcomer settings"
        ),
    )
    override val type = CommandTypes.UTILITIES
    override val minArgs = 1
    private val jda = main.jda

    override fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        when (args[0].lowercase()) {
            "prefix" -> {
                if (args.size < 2) {
                    val embedManager = EmbedManager("UniG0 Prefix", Color.BLUE, cmd, event.author)
                    embedManager.embedBuilder.addField(
                        "Current Prefix:", """`${FileUtils.getPrefix(event.guild.id, main)}`
You can also use ${jda.selfUser.asMention} as the prefix!""", false
                    )
                    event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
                    return
                }
                FileUtils.setPrefix(event.guild.id, args[1], main)
                event.channel.sendMessage("Successfully changed the prefix to `" + FileUtils.getPrefix(event.guild.id, main) + "`!")
                    .queue()
                return
            }
            "reload" -> {
                FileUtils.reload(main)
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
                if (FileUtils.getDisabledChannels(event.guild.id, main).contains(channel.id)) {
                    FileUtils.removeDisabledChannel(event.guild.id, channel.id, main)
                    event.channel.sendMessage("Successfully enabled the channel " + channel.asMention + "!")
                        .queue()
                    sendDisabledChannels(cmd, event)
                    return
                }
                FileUtils.addDisabledChannel(event.guild.id, channel.id, main)
                event.channel.sendMessage("Successfully disabled the channel " + channel.asMention + "!")
                    .queue()
                sendDisabledChannels(cmd, event)
                return
            }
            "disable-commands" -> {
                if (args.size < 2) {
                    sendDisabledCommands(cmd, event)
                    return
                }
                if (FileUtils.getDisabledCommands(event.guild.id, main).containsIgnoreCase(args[1])) {
                    FileUtils.enableCommand(event.guild.id, args[1], main)
                    event.channel.sendMessage("Successfully enabled the command `" + args[1] + "`!").queue()
                    sendDisabledCommands(cmd, event)
                    return
                }
                FileUtils.disableCommand(event.guild.id, args[1], main)
                event.channel.sendMessage("Successfully disabled the command `" + args[1] + "`!").queue()
                sendDisabledCommands(cmd, event)
                return
            }
            "delete-commands" -> {
                if (args.size < 2) {
                    sendAutoDeleteCmds(cmd, event)
                    return
                }
                if (FileUtils.getDeletedCommands(event.guild.id, main).contains(args[1])) {
                    FileUtils.removeDeletedCommand(event.guild.id, args[1], main)
                    event.channel.sendMessage("Successfully removed the command `" + args[1] + "`!").queue()
                    sendAutoDeleteCmds(cmd, event)
                    return
                }
                FileUtils.addDeletedCommand(event.guild.id, args[1], main)
                event.channel.sendMessage("Successfully added the command `" + args[1] + "`!").queue()
                sendAutoDeleteCmds(cmd, event)
                return
            }
            "welcomer" -> {
                if (args.size >= 2) {
                    return
                }
                val embedManager = EmbedManager("UniG0 Welcomer", Color.BLUE, cmd, event.author)
                embedManager.embedBuilder.addField(
                    "Enabled:",
                    """
                                        ```
                                        Public Message: ${FileUtils.isWelcomeMessageEnabled(event.guild.id, main)}
                                        DM Message: ${FileUtils.isWelcomeDMEnabled(event.guild.id, main)}```
                                        """.trimIndent(), false
                )
                embedManager.embedBuilder.addField(
                    "Messages:",
                    """
                                        ```
                                        Public Message: ${FileUtils.getWelcomeMessage(event.guild.id, main)}
                                        DM Message: ${FileUtils.getDMMessage(event.guild.id, main)}```
                                        """.trimIndent(), false
                )
                if (FileUtils.getWelcomeChannel(event.guild.id, main).getChannel(jda) == null) {
                    embedManager.embedBuilder.addField(
                        "Channels:",
                        "Public Message: NONE", false
                    )
                } else {
                    embedManager.embedBuilder.addField(
                        "Channels:",
                        "Public Message: " + (FileUtils.getWelcomeChannel(event.guild.id, main).getChannel(jda)?.asMention
                            ?: "None"),
                        false
                    )
                }
                event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
                return
            }
        }
    }

    private fun sendAutoDeleteCmds(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManager =
            EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.author)
        embedManager.embedBuilder.addField(
            FileUtils.getDeletedCommands(event.guild.id, main).getEmbedField("Commands that auto delete:")
        )
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }


    private fun sendDisabledChannels(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManager = EmbedManager("UniG0 Disabled Channels", Color.ORANGE, cmd, event.author)
        embedManager.embedBuilder.addField(
            FileUtils.getDisabledChannels(event.guild.id, main).getEmbedField("Channels that are disabled:")
        )
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }

    private fun sendDisabledCommands(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManager = EmbedManager("UniG0 Disabled Commands", Color.ORANGE, cmd, event.author)
        embedManager.embedBuilder.addField(
            FileUtils.getDisabledCommands(event.guild.id, main).getEmbedField("Commands that are disabled:")
        )
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }
}