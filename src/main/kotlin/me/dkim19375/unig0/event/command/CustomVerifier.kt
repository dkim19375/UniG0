package me.dkim19375.unig0.event.command

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.event.CustomListener
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.FileUtils
import me.dkim19375.unig0.util.function.containsIgnoreCase
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.Event

class CustomVerifier(private val bot: UniG0) : CustomListener() {
    override fun isValid(
        command: Command,
        cmd: String,
        args: List<String>,
        member: Member?,
        user: User,
        guild: Guild?,
        message: Message,
        channel: MessageChannel,
        event: Event
    ): Boolean {
        if (guild != null && FileUtils.getDisabledCommands(guild.id, bot).containsIgnoreCase(cmd)) {
            return false
        }
        if (user.isBot) {
            return false
        }
        if (guild != null && FileUtils.getDeletedCommands(guild.id, bot).containsIgnoreCase(cmd)) {
            message.delete().queue()
        }
        if (guild != null && FileUtils.getDeletedCommands(guild.id, bot).contains("*")) {
            message.delete().queue()
        }
        return super.isValid(command, cmd, args, member, user, guild, message, channel, event)
    }
}