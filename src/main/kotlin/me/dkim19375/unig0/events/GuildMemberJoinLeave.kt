package me.dkim19375.unig0.events

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class GuildMemberJoinLeave : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {}
}