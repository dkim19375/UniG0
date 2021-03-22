package me.dkim19375.unig0.events.commands

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.util.CommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.awt.Color
import java.util.*

class CustomEmbedCommands(jda: JDA) : CommandHandler(jda) {
    override fun onGuildMessageReceived(
        cmd: String,
        args: Array<String>,
        prefix: String,
        all: String,
        event: GuildMessageReceivedEvent
    ) {
        if (!cmd.equals("embed", ignoreCase = true)) {
            return
        }
        if (args.isEmpty()) {
            val types = setOf(
                "Title <title>",
                "TitleUrl <url>",
                "Description <description>",
                "Avatar <image-url>",
                "AuthorName <name>",
                "AuthorUrl <url>",
                "Thumbnail <image-url>",
                "Fields (too many params to show here,",
                "Image <image-url>",
                "Color <int/color>",
                "Footer <footer-text>",
                "Icon <image-url>",
                "import <json>",
                "export",
            )
            val embedManager = EmbedManager("UniG0 Embed Editor", Color.BLUE, cmd, event.author)
            embedManager.embedBuilder.addField(
                EmbedUtils.getEmbedGroup(
                    EntryImpl(
                        "Arguments:",
                        types
                    )
                )
            )
            return
        }
    }
}