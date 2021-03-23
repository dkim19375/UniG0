package me.dkim19375.unig0

import me.dkim19375.unig0.event.command.other.HelpCommand
import me.dkim19375.unig0.event.command.other.PingCommand
import me.dkim19375.unig0.event.command.utilities.AnnounceCommand
import me.dkim19375.unig0.event.command.utilities.CustomEmbedCommands
import me.dkim19375.unig0.event.command.utilities.OptionsCommand
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.FileManager
import me.dkim19375.unig0.util.property.GlobalProperties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import java.io.IOException
import java.util.*
import javax.security.auth.login.LoginException
import kotlin.concurrent.thread
import kotlin.system.exitProcess

object UniG0 {
    lateinit var fileManager: FileManager
        private set
    lateinit var jda: JDA
    lateinit var commands: Set<Command>

    @Throws(LoginException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        fileManager = FileManager()
        val builder = JDABuilder.createDefault(fileManager.globalConfig.get(GlobalProperties.token))
        val jda = builder.build()
        this.jda = jda
        registerCommands()
        jda.presence.activity = Activity.watching("dkim19375 code")
        thread {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNext()) {
                if (scanner.nextLine().equals("stop", ignoreCase = true)) {
                    println("Stopping the bot!")
                    jda.shutdown()
                    println("Stopped")
                    exitProcess(0)
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
        )
        commands.forEach(jda::addEventListener)
    }
}