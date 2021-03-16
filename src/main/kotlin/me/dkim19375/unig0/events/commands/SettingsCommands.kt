package me.dkim19375.unig0.events.commands

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.util.CommandHandler
import me.dkim19375.unig0.util.FileUtils
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color

class SettingsCommands(private val jda: JDA) : CommandHandler(jda) {
    override fun onGuildMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        if (!cmd.equals("options", ignoreCase = true)) {
            return
        }
        val options = mutableSetOf<String>()
        options.add("prefix")
        options.add("reload")
        options.add("delete-commands *|<command>")
        options.add("disable-channels <channel|id>")
        options.add("reset")
        options.add("welcomer WelcomeMessage <message>|DMMessage <message>|MessageEnabled <true/false>|DMEnabled <true/false>|channel <channel>")
        if (args.isEmpty()) {
            val embedManager = EmbedManager("UniG0 Options", Color.BLUE, cmd, event.author)
            embedManager.embedBuilder.addField(
                EmbedUtils.getEmbedGroup(
                    EntryImpl<String, Set<String>>(
                        "Options",
                        options
                    )
                )
            )
            event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
            return
        }
        when (args[0].toLowerCase()) {
            "prefix" -> {
                if (args.size < 2) {
                    val embedManagerPrefix = EmbedManager("UniG0 Prefix", Color.BLUE, cmd, event.author)
                    embedManagerPrefix.embedBuilder.addField(
                        "Current Prefix:", """`${FileUtils.getPrefix(event.guild.id)}`
You can also use ${jda.selfUser.asMention} as the prefix!""", true
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
                if (getChannel(args[1]) == null) {
                    event.channel.sendMessage("Invalid channel!").queue()
                    return
                }
                if (FileUtils.getDisabledChannels(event.guild.id).contains(getChannel(args[1])!!.id)) {
                    FileUtils.removeDisabledChannel(event.guild.id, getChannel(args[1])!!.id)
                    event.channel.sendMessage("Successfully enabled the channel " + getChannel(args[1])!!.asMention + "!")
                        .queue()
                    val embedManagerPrefix = EmbedManager("UniG0 Enabled Channels", Color.ORANGE, cmd, event.author)
                    embedManagerPrefix.embedBuilder.addField(
                        "Channels that are ignored:", """
     ${collectionNewLineChannels(FileUtils.getDisabledChannels(event.guild.id))}
     
     """.trimIndent(), true
                    )
                    event.channel.sendMessage(embedManagerPrefix.embedBuilder.build()).queue()
                    return
                }
                FileUtils.addDisabledChannel(event.guild.id, getChannel(args[1])!!.id)
                event.channel.sendMessage("Successfully disabled the channel " + getChannel(args[1])!!.asMention + "!")
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
     ${collectionNewLine(FileUtils.getDeletedCommands(event.guild.id))}
     
     """.trimIndent(), true
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
            "reset" -> {
                event.channel.sendMessage("Resetting all configurations!").queue()
                FileUtils.reset()
                event.channel.sendMessage("Successfully reset!").queue()
                return
            }
            "welcomer" -> {
                if (args.size < 2) {
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
                    if (getChannel(FileUtils.getWelcomeChannel(event.guild.id)) == null) {
                        embedManagerWelcomer.embedBuilder.addField(
                            "Channels:",
                            "Public Message: NONE", false
                        )
                    } else {
                        embedManagerWelcomer.embedBuilder.addField(
                            "Channels:",
                            "Public Message: " + getChannel(FileUtils.getWelcomeChannel(event.guild.id))!!.asMention,
                            false
                        )
                    }
                    event.channel.sendMessage(embedManagerWelcomer.embedBuilder.build()).queue()
                    return
                }
                sendInvalidOptionMessage(cmd, event, options)
            }
            else -> {
                sendInvalidOptionMessage(cmd, event, options)
            }
        }
    }

    private fun sendDisabledChannels(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManagerPrefix = EmbedManager("UniG0 Disabled Channels", Color.ORANGE, cmd, event.author)
        embedManagerPrefix.embedBuilder.addField(
            "Channels that are ignored:", """
     ${collectionNewLineChannels(FileUtils.getDisabledChannels(event.guild.id))}
     
     """.trimIndent(), true
        )
        event.channel.sendMessage(embedManagerPrefix.embedBuilder.build()).queue()
    }

    private fun sendAutoDeleteCmds(
        cmd: String,
        event: GuildMessageReceivedEvent
    ) {
        val embedManagerCMDDeletion =
            EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.author)
        embedManagerCMDDeletion.embedBuilder.addField(
            "Commands that auto-delete:", """
     ```${collectionNewLine(FileUtils.getDeletedCommands(event.guild.id))}
     ```
     """.trimIndent(), true
        )
        event.channel.sendMessage(embedManagerCMDDeletion.embedBuilder.build()).queue()
    }

    private fun sendInvalidOptionMessage(
        cmd: String,
        event: GuildMessageReceivedEvent,
        options: MutableSet<String>
    ) {
        event.channel.sendMessage("Invalid option!").queue()
        val embedManager = EmbedManager("UniG0 Options", Color.BLUE, cmd, event.author)
        embedManager.embedBuilder.addField(
            EmbedUtils.getEmbedGroup(
                EntryImpl<String, Set<String>>(
                    "Options",
                    options
                )
            )
        )
        event.channel.sendMessage(embedManager.embedBuilder.build()).queue()
    }

    private fun getChannel(ids: String): TextChannel? {
        var newString = ids
        newString = newString.replace("<", "")
        newString = newString.replace(">", "")
        newString = newString.replace("#", "")
        newString = newString.trim { it <= ' ' }
        return if (newString == "") {
            null
        } else jda.getTextChannelById(newString)
    }

    private fun collectionNewLine(collection: Collection<String>): String {
        val finished = StringBuilder()
        for (s in collection) {
            finished.append("\n").append(s)
        }
        return finished.toString()
    }

    private fun collectionNewLineChannels(collection: Collection<String>): String {
        val finished = StringBuilder()
        for (c in collection) {
            val s = getChannel(c)!!.asMention
            finished.append("\n").append(s)
        }
        return finished.toString()
    }
}