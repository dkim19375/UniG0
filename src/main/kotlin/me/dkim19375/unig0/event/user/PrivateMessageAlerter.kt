package me.dkim19375.unig0.event.user

import me.dkim19375.dkim19375jdautils.BotBase
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class PrivateMessageAlerter(private val bot: BotBase) : ListenerAdapter() {
    override fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {
        println("Private message from ${event.author.asTag} - ${event.author.id}: " +
                "${event.message.contentRaw} - display: ${event.message.contentStripped}")
    }
}