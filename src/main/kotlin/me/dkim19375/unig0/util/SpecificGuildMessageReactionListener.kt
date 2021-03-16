package me.dkim19375.unig0.util

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.util.*

class SpecificGuildMessageReactionListener(val jda: JDA, messages: Map<Message, ReactionEmote?>) : ListenerAdapter() {
    private val messages: Map<Message, Map<ReactionEmote?, Set<Runnable>>>
    fun getMessages(): Map<Message, Set<ReactionEmote?>> {
        val emoteMap: MutableMap<Message, Set<ReactionEmote?>> = HashMap()
        for (key in messages.keys) {
            val msg = messages[key]?: continue
            emoteMap[key] = msg.keys
        }
        return emoteMap
    }

    fun queueActionOnAdd(message: Message, emote: ReactionEmote, action: Runnable) {
        if (messages.containsKey(message)) {
            val emoteWithRunnables = messages[message]?: return
            if (emoteWithRunnables.containsKey(emote)) {
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
                if (reaction.asReactionCode == emote?.asReactionCode ?: false) {
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