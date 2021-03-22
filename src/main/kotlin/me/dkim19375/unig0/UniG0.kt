package me.dkim19375.unig0

import me.dkim19375.unig0.events.Messagers
import me.dkim19375.unig0.events.commands.CustomEmbedCommands
import me.dkim19375.unig0.events.commands.MiscMessages
import me.dkim19375.unig0.events.commands.SettingsCommands
import me.dkim19375.unig0.util.FileManager
import me.dkim19375.unig0.util.properties.GlobalProperties
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import java.io.IOException
import java.util.*
import javax.security.auth.login.LoginException
import kotlin.system.exitProcess

object UniG0 {
    lateinit var fileManager: FileManager
        private set

    @Throws(LoginException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        fileManager = FileManager()
        println("token: ${fileManager.globalConfig.get(GlobalProperties.token)}")
        val builder = JDABuilder.createDefault(fileManager.globalConfig.get(GlobalProperties.token))
        val jda = builder.build()
        jda.presence.activity = Activity.watching("dkim19375 code")
        jda.addEventListener(MiscMessages(jda))
        jda.addEventListener(SettingsCommands(jda))
        jda.addEventListener(Messagers(jda))
        jda.addEventListener(CustomEmbedCommands(jda))
        Thread().run {
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
}