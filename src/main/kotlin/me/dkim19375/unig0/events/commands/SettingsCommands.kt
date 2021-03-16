package me.dkim19375.unig0.events.commands;

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager;
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils;
import me.dkim19375.dkim19375jdautils.impl.EntryImpl;
import me.dkim19375.unig0.util.CommandHandler;
import me.dkim19375.unig0.util.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
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
        options.add("disable-channels <channel|id>");
        options.add("reset");
        options.add("welcomer WelcomeMessage <message>|DMMessage <message>|MessageEnabled <true/false>|DMEnabled <true/false>|channel <channel>");

        if (args.length < 1) {
            EmbedManager embedManager = new EmbedManager("UniG0 Options", Color.BLUE, cmd, event.getAuthor());
            embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Options", options)));
            event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
            return;
        }
        switch (args[0].toLowerCase()) {
            case "prefix":
                if (args.length < 2) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Prefix", Color.BLUE, cmd, event.getAuthor());
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
            case "disable-channels":
                if (args.length < 2) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Disabled Channels", Color.ORANGE, cmd, event.getAuthor());
                    embedManagerPrefix.getEmbedBuilder().addField("Channels that are ignored:", ""
                            + collectionNewLineChannels(FileUtils.getDisabledChannels(event.getGuild().getId())) + "\n", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                if (getChannel(args[1]) == null) {
                    event.getChannel().sendMessage("Invalid channel!").queue();
                    return;
                }
                if (FileUtils.getDisabledChannels(event.getGuild().getId()).contains(getChannel(args[1]).getId())) {
                    FileUtils.removeDisabledChannel(event.getGuild().getId(), getChannel(args[1]).getId());
                    event.getChannel().sendMessage("Successfully enabled the channel " + getChannel(args[1]).getAsMention() + "!").queue();
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Enabled Channels", Color.ORANGE, cmd, event.getAuthor());
                    embedManagerPrefix.getEmbedBuilder().addField("Channels that are ignored:", ""
                            + collectionNewLineChannels(FileUtils.getDisabledChannels(event.getGuild().getId())) + "\n", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.addDisabledChannel(event.getGuild().getId(), getChannel(args[1]).getId());
                event.getChannel().sendMessage("Successfully disabled the channel " + getChannel(args[1]).getAsMention() + "!").queue();
                EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Disabled Channels", Color.ORANGE, cmd, event.getAuthor());
                embedManagerPrefix.getEmbedBuilder().addField("Channels that are ignored:", ""
                        + collectionNewLineChannels(FileUtils.getDisabledChannels(event.getGuild().getId())) + "\n", true);
                event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                return;
            case "delete-commands":
                if (args.length < 2) {
                    EmbedManager embedManagerCMDDeletion = new EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.getAuthor());
                    embedManagerCMDDeletion.getEmbedBuilder().addField("Commands that auto-delete:", ""
                            + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n", true);
                    event.getChannel().sendMessage(embedManagerCMDDeletion.getEmbedBuilder().build()).queue();
                    return;
                }
                if (FileUtils.getDeletedCommands(event.getGuild().getId()).contains(args[1])) {
                    FileUtils.removeDeletedCommand(event.getGuild().getId(), args[1]);
                    event.getChannel().sendMessage("Successfully removed the command `" + args[1] + "`!").queue();
                    EmbedManager embedManagerCMDDeletion = new EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.getAuthor());
                    embedManagerCMDDeletion.getEmbedBuilder().addField("Commands that auto-delete:", "```"
                            + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n```", true);
                    event.getChannel().sendMessage(embedManagerCMDDeletion.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.addDeletedCommand(event.getGuild().getId(), args[1]);
                event.getChannel().sendMessage("Successfully added the command `" + args[1] + "`!").queue();
                EmbedManager embedManagerCMDDeletion = new EmbedManager("UniG0 Command Deletion", Color.ORANGE, cmd, event.getAuthor());
                embedManagerCMDDeletion.getEmbedBuilder().addField("Commands that auto-delete:", "```"
                        + collectionNewLine(FileUtils.getDeletedCommands(event.getGuild().getId())) + "\n```", true);
                event.getChannel().sendMessage(embedManagerCMDDeletion.getEmbedBuilder().build()).queue();
                return;
            case "reset":
                event.getChannel().sendMessage("Resetting all configurations!").queue();
                FileUtils.reset();
                event.getChannel().sendMessage("Successfully reset!").queue();
                return;
            case "welcomer":
                if (args.length < 2) {
                    EmbedManager embedManagerWelcomer = new EmbedManager("UniG0 Welcomer", Color.BLUE, cmd, event.getAuthor());
                    embedManagerWelcomer.getEmbedBuilder().addField("Enabled:",
                            "```\nPublic Message: " + FileUtils.isWelcomeMessageEnabled(event.getGuild().getId())
                            + "\nDM Message: " + FileUtils.isWelcomeDMEnabled(event.getGuild().getId()) + "```", false);
                    embedManagerWelcomer.getEmbedBuilder().addField("Messages:",
                            "```\nPublic Message: " + FileUtils.getWelcomeMessage(event.getGuild().getId())
                                    + "\nDM Message: " + FileUtils.getDMMessage(event.getGuild().getId()) + "```", false);
                    if (getChannel(FileUtils.getWelcomeChannel(event.getGuild().getId())) == null) {
                        embedManagerWelcomer.getEmbedBuilder().addField("Channels:",
                                "Public Message: NONE", false);
                    } else {
                        embedManagerWelcomer.getEmbedBuilder().addField("Channels:",
                                "Public Message: " + getChannel(FileUtils.getWelcomeChannel(event.getGuild().getId())).getAsMention(), false);
                    }
                    event.getChannel().sendMessage(embedManagerWelcomer.getEmbedBuilder().build()).queue();
                    return;
                }
            default:
                event.getChannel().sendMessage("Invalid option!").queue();
                EmbedManager embedManager = new EmbedManager("UniG0 Options", Color.BLUE, cmd, event.getAuthor());
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Options", options)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
        }
    }

    public TextChannel getChannel(final String ids) {
        String newString = ids;
        newString = newString.replace("<", "");
        newString = newString.replace(">", "");
        newString = newString.replace("#", "");
        newString = newString.trim();
        if (newString.equals("")) {
            return null;
        }
        return getJDA().getTextChannelById(newString);
    }

    public String collectionNewLine(final Collection<String> collection) {
        StringBuilder finished = new StringBuilder();
        for (String s : collection) {
            finished.append("\n").append(s);
        }
        return finished.toString();
    }

    public String collectionNewLineChannels(final Collection<String> collection) {
        StringBuilder finished = new StringBuilder();
        for (String c : collection) {
            String s = getChannel(c).getAsMention();
            finished.append("\n").append(s);
        }
        return finished.toString();
    }
}
