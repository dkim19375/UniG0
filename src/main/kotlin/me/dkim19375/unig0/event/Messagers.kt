package me.dkim19375.unig0.event

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.FileUtils
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Messagers(private val main: UniG0) : ListenerAdapter() {
    private val jda = main.jda

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (FileUtils.isWelcomeDMEnabled(event.guild.id)) {
            welcomeDM(event)
        }
        if (FileUtils.isWelcomeMessageEnabled(event.guild.id)) {
            welcomeMessage(event)
        }
    }

    private fun welcomeDM(event: GuildMemberJoinEvent) {
        val message = parsePlaceholders(FileUtils.getDMMessage(event.guild.id), event)
        event.user.openPrivateChannel().queue { channel: PrivateChannel -> channel.sendMessage(message).queue() }
    }

    private fun welcomeMessage(event: GuildMemberJoinEvent) {
        if (getChannel(FileUtils.getWelcomeChannel(event.guild.id)) == null) {
            return
        }
        val textChannel = getChannel(FileUtils.getWelcomeChannel(event.guild.id))?: return
        textChannel.sendMessage(FileUtils.getWelcomeMessage(event.guild.id)).queue()
    }

    private fun getChannel(ids: String): TextChannel? {
        var newString = ids
        newString = newString.replace("<", "")
        newString = newString.replace(">", "")
        newString = newString.replace("#", "")
        newString = newString.trim { it <= ' ' }
        return if (newString == "") {
            null
        } else jda.getTextChannelById(newString)
    }

    private fun parsePlaceholders(string: String, e: GuildMemberJoinEvent): String {
        var parsed = string
        parsed = parsed.replace("\\{ServerName}".toRegex(), e.guild.name)
        parsed = parsed.replace("\\{user}".toRegex(), e.user.asMention)
        parsed = parsed.replace("\\{members}".toRegex(), e.guild.members.size.toString())
        return parsed
    }
}