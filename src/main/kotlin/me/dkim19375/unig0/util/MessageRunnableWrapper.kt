package me.dkim19375.unig0.util

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote

class MessageRunnableWrapper(
    var message: Message,
    private var emotes: MutableMap<ReactionEmote, MutableSet<Runnable>>
) {
    fun getEmotes(): Map<ReactionEmote, MutableSet<Runnable>> {
        return emotes
    }

    fun setEmotes(emotes: MutableMap<ReactionEmote, MutableSet<Runnable>>) {
        this.emotes = emotes
    }

    fun addEmote(emote: Map.Entry<ReactionEmote, MutableSet<Runnable>>) {
        if (!emotes.containsKey(emote.key)) {
            emotes[emote.key] = emote.value
            return
        }
        emotes[emote.key]!!.addAll(emote.value)
    }
}