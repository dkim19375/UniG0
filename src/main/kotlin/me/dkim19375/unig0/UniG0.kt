package me.dkim19375.unig0

import me.dkim19375.dkim19375jdautils.BotBase
import me.dkim19375.dkim19375jdautils.command.HelpCommand
import me.dkim19375.dkim19375jdautils.event.CustomListener
import me.dkim19375.unig0.event.command.CommandTypes
import me.dkim19375.unig0.event.command.CustomVerifier
import me.dkim19375.unig0.event.command.`fun`.AnnoyCommand
import me.dkim19375.unig0.event.command.other.OptionsCommand
import me.dkim19375.unig0.event.command.other.PingCommand
import me.dkim19375.unig0.event.command.other.StopCommand
import me.dkim19375.unig0.event.command.other.TestCommand
import me.dkim19375.unig0.event.command.utilities.AnnounceCommand
import me.dkim19375.unig0.event.command.utilities.CustomEmbedCommands
import me.dkim19375.unig0.event.user.PrivateMessageAlerter
import me.dkim19375.unig0.util.FileManager
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.property.GlobalProperties
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent

fun main() {
    val fileManager = FileManager()
    val bot = UniG0(fileManager)
    bot.intents.add(GatewayIntent.GUILD_MEMBERS)
    bot.onStart()
    bot.registerCommands()
    bot.consoleCommands["message"] = bot::messageCommand
    bot.jda.presence.activity = Activity.watching("dkim19375 code")
    bot.jda.awaitReady()
}

class UniG0(val fileManager: FileManager) : BotBase() {
    override val customListener: CustomListener = CustomVerifier(this)
    override val name = "UniG0"
    override val token = fileManager.globalConfig.get(GlobalProperties.token)

    fun messageCommand(next: String) {
        val args = next.split(" ").drop(1)
        if (args.size < 2) {
            println("Too little args! Usage: message <channel> <message>")
            return
        }
        val channelId = args[0].toLongOrNull()
        val message = args.drop(1).joinToString(" ")
        if (channelId == null) {
            println("Invalid channel! Usage: message <channel> <message>")
            return
        }
        jda.getTextChannelById(channelId)?.let { channel ->
            channel.sendMessage(message).queue()
            return@messageCommand
        }
        jda.getPrivateChannelById(channelId)?.let { channel ->
            channel.sendMessage(message).queue()
            return@messageCommand
        }
        jda.openPrivateChannelById(channelId).queue({ channel ->
            channel.sendMessage(message).queue()
        }, { error ->
            throw error
        })
    }

    fun registerCommands() {
        commandTypes.clear()
        commandTypes.addAll(CommandTypes.values())
        commands.clear()
        commands.addAll(
            listOf(
                HelpCommand(this),
                PingCommand(this),
                AnnounceCommand(this),
                CustomEmbedCommands(this),
                OptionsCommand(this),
                StopCommand(this),
                AnnoyCommand(this),
                TestCommand(this)
            )
        )
        jda.addEventListener(
            PrivateMessageAlerter(this)
        )
    }

    override fun getPrefix(guild: String): String {
        return FileUtils.getPrefix(guild, this)
    }
}