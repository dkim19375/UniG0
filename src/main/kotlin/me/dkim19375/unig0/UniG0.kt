package me.dkim19375.unig0

import me.dkim19375.unig0.event.EventListener
import me.dkim19375.unig0.event.command.other.*
import me.dkim19375.unig0.event.command.utilities.AnnounceCommand
import me.dkim19375.unig0.event.command.utilities.CustomEmbedCommands
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.FileManager
import me.dkim19375.unig0.util.property.GlobalProperties
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

    @JvmStatic
    fun main(args: Array<String>) {
        fileManager = FileManager()
        startBot()
    }

    fun startBot() {
        if (this::jda.isInitialized) {
            println("Stopping the bot!")
            jda.shutdown()
            println("Stopped")
            restart = true
        }
        println("Starting bot")
        val builder = JDABuilder.createDefault(fileManager.globalConfig.get(GlobalProperties.token))
        val jda = builder.build()
        this.jda = jda
        jda.presence.activity = Activity.watching("dkim19375 code")
        registerCommands()
        if (restart) {
            return
        }
        thread {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNext()) {
                if (scanner.nextLine().equals("stop", ignoreCase = true)) {
                    println("Stopping the bot!")
                    jda.shutdown()
                    println("Stopped")
                    exitProcess(0)
                }
                if (scanner.nextLine().equals("restart", ignoreCase = true)) {
                    println("Restarting the bot!")
                    startBot()
                    println("Bot restarted!")
                }
            }
        }
    }

    private fun registerCommands() {
        commands = setOf(
            HelpCommand(this),
            PingCommand(this),
            AnnounceCommand(this),
            CustomEmbedCommands(this),
            OptionsCommand(this),
            RestartCommand(this),
            StopCommand(this),
        )
        jda.addEventListener(EventListener(this))
    }

    fun sendEvent(event: (Command) -> Unit) {
        commands.forEach(event)
    }
}