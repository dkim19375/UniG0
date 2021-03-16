package me.dkim19375.unig0.util

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
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import me.dkim19375.unig0.util.FileManager
import javax.security.auth.login.LoginException
import kotlin.jvm.JvmStatic
import net.dv8tion.jda.api.JDABuilder
import me.dkim19375.unig0.events.commands.MiscMessages
import me.dkim19375.unig0.events.commands.SettingsCommands
import me.dkim19375.unig0.events.Messagers
import me.dkim19375.unig0.events.commands.CustomEmbedCommands
import net.dv8tion.jda.api.entities.*

class SpecificGuildMessageReactionListener(val jda: JDA, messages: Map<Message, ReactionEmote?>) : ListenerAdapter() {
    private val messages: Map<Message, Map<ReactionEmote?, Set<Runnable>>>
    fun getMessages(): Map<Message, Set<ReactionEmote?>> {
        val emoteMap: MutableMap<Message, Set<ReactionEmote?>> = HashMap()
        for (key in messages.keys) {
            emoteMap[key] = messages[key]!!.keys
        }
        return emoteMap
    }

    fun queueActionOnAdd(message: Message, emote: ReactionEmote?, action: Runnable?) {
        if (messages.containsKey(message)) {
            val emoteWithRunnables = messages[message]!!
            if (messages[message]!!.containsKey(emote)) {
            }
        }
    }

    fun onMessageReactionAdd(event: GuildMessageReactionAddEvent) {}
    fun onMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {}
    fun onMessageReactionRemoveAll(event: GuildMessageReactionRemoveAllEvent) {}
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        if (!isValid(event.messageIdLong, event.reactionEmote)) {
            return
        }
        onMessageReactionAdd(event)
    }

    override fun onGuildMessageReactionRemove(event: GuildMessageReactionRemoveEvent) {
        if (!isValid(event.messageIdLong, event.reactionEmote)) {
            return
        }
        onMessageReactionRemove(event)
    }

    override fun onGuildMessageReactionRemoveAll(event: GuildMessageReactionRemoveAllEvent) {
        if (!isValid(event.messageIdLong, null)) {
            return
        }
        onMessageReactionRemoveAll(event)
    }

    fun isValid(messageId: Long, reaction: ReactionEmote?): Boolean {
        var containsMessage = false
        for (message in messages.keys) {
            if (messageId == message.idLong) {
                containsMessage = true
            }
        }
        if (!containsMessage) {
            return false
        }
        if (reaction == null) {
            return true
        }
        var containsEmote = false
        for (emoteAndRunnables in messages.values) {
            for (emote in emoteAndRunnables.keys) {
                if (reaction.asReactionCode == emote!!.asReactionCode) {
                    containsEmote = true
                }
            }
        }
        return containsEmote
    }

    init {
        val map: MutableMap<Message, Map<ReactionEmote?, Set<Runnable>>> = HashMap()
        for (key in messages.keys) {
            val m: MutableMap<ReactionEmote?, Set<Runnable>> = HashMap()
            m[messages[key]] = HashSet()
            map[key] = m
        }
        this.messages = map
    }
}