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
    /*    private final List<MessageEmbed.Field> fields = new LinkedList<>();
    private final StringBuilder description = new StringBuilder();
    private int color = Role.DEFAULT_COLOR_RAW;
    private String url, title;
    private OffsetDateTime timestamp;
    private MessageEmbed.Thumbnail thumbnail;
    private MessageEmbed.AuthorInfo author;
    private MessageEmbed.Footer footer;
    private MessageEmbed.ImageInfo image;*/
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
        if (args.size < 1) {
            val types: MutableSet<String> = HashSet()
            types.add("Title <title>")
            types.add("TitleUrl <url>")
            types.add("Description <description>")
            types.add("Avatar <image-url>")
            types.add("AuthorName <name>")
            types.add("AuthorUrl <url>")
            types.add("Thumbnail <image-url>")
            types.add("Fields (too many params to show here)")
            types.add("Image <image-url>")
            types.add("Color <int/color>")
            types.add("Footer <footer-text>")
            types.add("Icon <image-url>")
            types.add("import <json>")
            types.add("export")
            val embedManager = EmbedManager("UniG0 Embed Editor", Color.BLUE, cmd, event.author)
            embedManager.embedBuilder.addField(
                EmbedUtils.getEmbedGroup(
                    EntryImpl<String, Set<String>>(
                        "Arguments:",
                        types
                    )
                )
            )
            return
        }
    }
}