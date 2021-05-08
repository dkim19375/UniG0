package me.dkim19375.unig0.event.command.utilities

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class CustomEmbedCommands(private val main: UniG0) : Command(main.jda) {
    override val command = "embed"
    override val name = "Embed Editor"
    override val aliases = setOf<String>()
    override val description = "Makes a custom embed"
    override val arguments = setOf(
        CommandArg(this, "Title <title>", "Edit the title in the embed"),
        CommandArg(this, "TitleUrl <url>", "Edit the title in the embed"),
        CommandArg(this, "Description <description>", "Edit the title in the embed"),
        CommandArg(this, "Avatar <image-url>", "Edit the title in the embed"),
        CommandArg(this, "AuthorName <name>", "Edit the title in the embed"),
        CommandArg(this, "AuthorUrl <url>", "Edit the title in the embed"),
        CommandArg(this, "Thumbnail <image-url>", "Edit the title in the embed"),
        CommandArg(this, "Fields (too many params to show here", "Edit the title in the embed"),
        CommandArg(this, "Image <image-url>", "Edit the title in the embed"),
        CommandArg(this, "Color <int/color>", "Edit the title in the embed"),
        CommandArg(this, "Footer <footer-text>", "Edit the title in the embed"),
        CommandArg(this, "Icon <image-url>", "Edit the title in the embed"),
        CommandArg(this, "import <json>", "Edit the title in the embed"),
        CommandArg(this, "export", "Edit the title in the embed"),
    )
    override val type = CommandType.UTILITIES
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

    }
}