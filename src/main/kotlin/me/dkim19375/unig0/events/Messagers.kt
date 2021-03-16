package me.dkim19375.unig0.events

import net.dv8tion.jda.api.entities.MessageEmbed
import java.util.LinkedList
import java.lang.StringBuilder
import java.time.OffsetDateTime
import net.dv8tion.jda.api.entities.MessageEmbed.Thumbnail
import net.dv8tion.jda.api.entities.MessageEmbed.AuthorInfo
import net.dv8tion.jda.api.entities.MessageEmbed.Footer
import net.dv8tion.jda.api.entities.MessageEmbed.ImageInfo
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.properties.GlobalProperties
import me.dkim19375.unig0.util.properties.ServerProperties
import me.mattstudios.config.SettingsHolder
import java.util.HashSet
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.datafiles.Embed
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.HashMap
import java.nio.file.Paths
import java.io.IOException
import kotlin.Throws
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter
import me.dkim19375.dkim19375jdautils.holders.MessageRecievedHolder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.lang.IllegalStateException
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote
import java.lang.Runnable
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent
import me.dkim19375.unig0.util.CommandHandler
import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import java.awt.Color
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.entities.PrivateChannel
import me.dkim19375.unig0.util.FileManager
import javax.security.auth.login.LoginException
import kotlin.jvm.JvmStatic
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import me.dkim19375.unig0.events.commands.MiscMessages
import me.dkim19375.unig0.events.commands.SettingsCommands
import me.dkim19375.unig0.events.Messagers
import me.dkim19375.unig0.events.commands.CustomEmbedCommands

class Messagers(private val jda: JDA) : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (FileUtils.isWelcomeDMEnabled(event.guild.id)) {
            welcomeDM(event)
        }
        if (FileUtils.isWelcomeMessageEnabled(event.guild.id)) {
            welcomeMessage(event)
        }
    }

    fun welcomeDM(event: GuildMemberJoinEvent) {
        val message = parsePlaceholders(FileUtils.getDMMessage(event.guild.id), event)
        event.user.openPrivateChannel().queue { channel: PrivateChannel -> channel.sendMessage(message).queue() }
    }

    fun welcomeMessage(event: GuildMemberJoinEvent) {
        if (getChannel(FileUtils.getWelcomeChannel(event.guild.id)) == null) {
            return
        }
        val textChannel = getChannel(FileUtils.getWelcomeChannel(event.guild.id))
        textChannel!!.sendMessage(FileUtils.getWelcomeMessage(event.guild.id)).queue()
    }

    fun getChannel(ids: String?): TextChannel? {
        var newString = ids
        newString = newString!!.replace("<", "")
        newString = newString.replace(">", "")
        newString = newString.replace("#", "")
        newString = newString.trim { it <= ' ' }
        return if (newString == "") {
            null
        } else jda.getTextChannelById(newString)
    }

    fun parsePlaceholders(string: String?, e: GuildMemberJoinEvent): String {
        var parsed = string
        parsed = parsed!!.replace("\\{ServerName}".toRegex(), e.guild.name)
        parsed = parsed.replace("\\{user}".toRegex(), e.user.asMention)
        parsed = parsed.replace("\\{members}".toRegex(), e.guild.members.size.toString())
        return parsed
    }
}