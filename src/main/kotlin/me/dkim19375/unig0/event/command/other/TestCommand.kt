package me.dkim19375.unig0.event.command.other

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.dkim19375jdautils.data.Whitelist
import me.dkim19375.dkim19375jdautils.util.EventType
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.event.command.CommandTypes
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

private const val THUMBS_UP = "\uD83D\uDC4D"
class TestCommand(private val bot: UniG0) : Command(bot) {
    override val command = "test"
    override val name = "Test"
    override val aliases = setOf<String>()
    override val description = "Testing features"
    override val arguments = setOf<CommandArg>()
    override val type = CommandTypes.OTHER
    override val minArgs = 0
    private val jda = bot.jda

    override fun onCommand(cmd: String, args: List<String>, prefix: String, all: String, event: MessageReceivedEvent) {
        event.channel.sendMessage("React here!").queue { msg ->
            msg.addReaction(THUMBS_UP).queue {
                bot.eventsManager.onReactionAdd(
                    permanent = false,
                    eventType = EventType.GENERIC,
                    action = action@{ _: Event,
                               _: Guild?,
                               _: MessageReaction.ReactionEmote,
                               channel: MessageChannel,
                               _: User,
                               _: Message,
                               _: Member? ->
                        channel.sendMessage("Reacted!").queue()
                        return@action true
                    },
                    requiredMessage = msg.idLong,
                    requiredChannel = msg.channel.idLong,
                    whitelist = Whitelist(
                        jda = jda,
                        whitelist = setOf(event.author.idLong),
                        ignoreWhitelistBots = false,
                        ignoreWhitelistSelf = false
                    ),
                    removeIfNoPerms = true,
                    removeBotIfNoPerms = true,
                    debug = false
                )
                bot.eventsManager.onReactionAdd(
                    permanent = true,
                    eventType = EventType.GENERIC,
                    action = action@{ _: Event,
                               _: Guild?,
                               _: MessageReaction.ReactionEmote,
                               channel: MessageChannel,
                               user: User,
                               message: Message,
                               _: Member? ->
                        channel.sendMessage("Reacted to thumbs up!").queue()
                        message.removeReaction(THUMBS_UP, user).queue()
                        return@action true
                    },
                    requiredMessage = msg.idLong,
                    requiredChannel = msg.channel.idLong,
                    whitelist = Whitelist(
                        jda = jda,
                        whitelist = setOf(event.author.idLong),
                        ignoreWhitelistBots = false,
                        ignoreWhitelistSelf = false
                    ),
                    reaction = MessageReaction.ReactionEmote.fromUnicode(THUMBS_UP, jda),
                    removeIfNoPerms = true,
                    removeBotIfNoPerms = true,
                    debug = false
                )
            }
        }
    }
}