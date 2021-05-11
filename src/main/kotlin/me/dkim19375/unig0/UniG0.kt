package me.dkim19375.unig0

import me.dkim19375.unig0.event.EventListener
import me.dkim19375.unig0.event.command.`fun`.AnnoyCommand
import me.dkim19375.unig0.event.command.other.HelpCommand
import me.dkim19375.unig0.event.command.other.OptionsCommand
import me.dkim19375.unig0.event.command.other.PingCommand
import me.dkim19375.unig0.event.command.other.StopCommand
import me.dkim19375.unig0.event.command.utilities.AnnounceCommand
import me.dkim19375.unig0.event.command.utilities.CustomEmbedCommands
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.FileManager
import me.dkim19375.unig0.util.FileUtils
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

object UniG0 {
    lateinit var fileManager: FileManager
        private set
    lateinit var jda: JDA
    lateinit var commands: Set<Command>
    var restart = false
    var stopped = false

    @JvmStatic
    fun main(args: Array<String>) {
        fileManager = FileManager()
        startBot()
    }

    private fun startBot() {
        if (this::jda.isInitialized) {
            println("Stopping the bot!")
            jda.shutdown()
            println("Stopped")
            restart = true
        }
        println("Starting bot")
        val builder = JDABuilder.createDefault(FileUtils.token)
        val jda = builder.build()
        this.jda = jda
        jda.presence.activity = Activity.watching("dkim19375 code")
        registerCommands()
        if (restart) {
            return
        }
        try {
            Runtime.getRuntime().addShutdownHook(thread(false) {
                if (!stopped) {
                    println("Stopping the bot!")
                    jda.shutdown()
                    println("Stopped")
                    stopped = true
                }
            })
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
        thread {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNext()) {
                val next = scanner.nextLine()
                if (next.equals("stop", ignoreCase = true)) {
                    println("Stopping the bot!")
                    jda.shutdown()
                    println("Stopped")
                    stopped = true
                    exitProcess(0)
                }
                if (next.startsWith("message", ignoreCase = true)) {
                    messageCommand(next)
                }
            }
        }
    }

    private fun messageCommand(next: String) {
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
            return
        }
        jda.getPrivateChannelById(channelId)?.let { channel ->
            channel.sendMessage(message).queue()
            return
        }
        jda.openPrivateChannelById(channelId).queue ({ channel ->
            channel.sendMessage(message).queue()
        }, { error ->
            throw error
        })
        println("Invalid channel!")
    }

    private fun registerCommands() {
        commands = setOf(
            HelpCommand(this),
            PingCommand(this),
            AnnounceCommand(this),
            CustomEmbedCommands(this),
            OptionsCommand(this),
            StopCommand(this),
            AnnoyCommand(this)
        )
        jda.addEventListener(EventListener(this))
    }

    fun sendEvent(event: (Command) -> Unit) = commands.forEach(event)
}