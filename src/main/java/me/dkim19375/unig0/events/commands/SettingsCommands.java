package me.dkim19375.unig0.events.commands;

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager;
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils;
import me.dkim19375.dkim19375jdautils.impl.EntryImpl;
import me.dkim19375.unig0.util.CommandHandler;
import me.dkim19375.unig0.util.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SettingsCommands extends CommandHandler {
    public SettingsCommands(JDA jda) {
        super(jda);
    }

    @Override
    public void onGuildMessageReceived(String cmd, String[] args, String prefix, String all, GuildMessageReceivedEvent event) {
        if (!cmd.equalsIgnoreCase("options")) {
            return;
        }
        Set<String> options = new HashSet<>();
        options.add("prefix");
        options.add("reload");
        options.add("delete-commands *|<command>");

        if (args.length < 1) {
            EmbedManager embedManager = new EmbedManager("UniG0 Options", null, Color.BLUE, cmd, null);
            embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Options", options)));
            event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
            return;
        }
        switch (args[0].toLowerCase()) {
            case "prefix":
                if (args.length < 2) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Prefix", null, Color.BLUE, cmd, null);
                    embedManagerPrefix.getEmbedBuilder().addField("Current Prefix:", "`" + FileUtils.getPrefix(event.getGuild().getId()) + "`\nYou can also use "
                            + getJDA().getSelfUser().getAsMention() + " as the prefix!", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.setPrefix(event.getGuild().getId(), args[1]);
                event.getChannel().sendMessage("Successfully changed the prefix to `" + FileUtils.getPrefix(event.getGuild().getId()) + "`!").queue();
                return;
            case "reload":
                FileUtils.reload();
                event.getChannel().sendMessage("Successfully reloaded files!").queue();
                return;
            case "delete-commands":
                if (args.length < 2) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Command Deletion", null, Color.ORANGE, cmd, null);
                    embedManagerPrefix.getEmbedBuilder().addField("Commands that auto-delete:", "```"
                            + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n```", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                if (FileUtils.getDeletedCommands(event.getGuild().getId()).contains(args[1])) {
                    FileUtils.removeDeletedCommand(event.getGuild().getId(), args[1]);
                    event.getChannel().sendMessage("Successfully removed the command `" + args[1] + "`!").queue();
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Command Deletion", null, Color.ORANGE, cmd, null);
                    embedManagerPrefix.getEmbedBuilder().addField("Commands that auto-delete:", "```"
                            + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n```", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.addDeletedCommand(event.getGuild().getId(), args[1]);
                event.getChannel().sendMessage("Successfully added the command `" + args[1] + "`!").queue();
                EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Command Deletion", null, Color.ORANGE, cmd, null);
                embedManagerPrefix.getEmbedBuilder().addField("Commands that auto-delete:", "```"
                        + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n```", true);
                event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                return;
            default:
                event.getChannel().sendMessage("Invalid option!").queue();
                EmbedManager embedManager = new EmbedManager("UniG0 Options", null, Color.BLUE, cmd, null);
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Options", options)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
        }
    }

    public String collectionNewLine(Collection<String> collection) {
        StringBuilder finished = new StringBuilder();
        for (String s : collection) {
            finished.append("\n").append(s);
        }
        return finished.toString();
    }
}
