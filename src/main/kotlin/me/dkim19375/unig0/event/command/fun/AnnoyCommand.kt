package me.dkim19375.unig0.event.command.`fun`

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class AnnoyCommand(main: UniG0) : Command(main) {
    override val command = "annoy"
    override val name = "Annoy"
    override val aliases = setOf<String>()
    override val description = "Annoy someone by pinging them in all channels!"
    override val arguments = setOf(CommandArg(this, "<user>", "The user to annoy"))
    override val type = CommandTypes.FUN
    override val minArgs = 1
    override val permissions = setOf(Permission.MESSAGE_MANAGE)
    private val jda = main.jda

    override fun onGuildCommand(
        cmd: String,
        args: List<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        val userStr = args[0].filter { s -> s.toString().toIntOrNull() != null }
        val userId = userStr.toLongOrNull()
        val member = event.member
        if (userId == null || member == null) {
            event.channel.sendMessage("Invalid ID or mention!").queue()
            return
        }
        val userCached = event.guild.getMemberById(userId)
        if (userCached != null) {
            annoy(event.guild, member, userCached)
            return
        }
        event.guild.retrieveMemberById(userId, false).queue { retrievedUser ->
            if (retrievedUser != null) {
                annoy(event.guild, member, retrievedUser)
                return@queue
            }
            event.channel.sendMessage("Invalid ID or mention!").queue()
        }
    }

    private fun annoy(guild: Guild, from: Member, to: Member) {
        val channels = guild.textChannels.toSet()
        val talkChannels = mutableSetOf<TextChannel>()
        for (channel in channels) {
            if (channel.canTalk(from) && channel.canTalk(to)) {
                talkChannels.add(channel)
            }
        }
        for (channel in talkChannels) {
            channel.sendMessage(to.user.asMention).queue { message ->
                message.delete().queue()
            }
        }
    }
}