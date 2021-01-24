package me.dkim19375.unig0.events.commands;

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager;
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils;
import me.dkim19375.dkim19375jdautils.impl.EntryImpl;
import me.dkim19375.unig0.util.CommandHandler;
import me.dkim19375.unig0.util.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MiscMessages extends CommandHandler {

    public MiscMessages(JDA jda) {
        super(jda);
    }

    @Override
    public void onGuildMessageReceived(String cmd, String[] args, String prefix, String all, GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        final Set<String> commands = new HashSet<>();
        if (FileUtils.getDeletedCommands(event.getGuild().getId()).contains(cmd)) {
            event.getMessage().delete().queue();
        }
        if (FileUtils.getDeletedCommands(event.getGuild().getId()).contains("*")) {
            event.getMessage().delete().queue();
        }
        commands.add("help");
        commands.add("options");
        commands.add("ping");
        commands.add("announce <channel> <message>");
        switch (cmd.toLowerCase()) {
            case "help":
                EmbedManager embedManager = new EmbedManager("UniG0 Help", Color.BLUE, cmd, event.getAuthor());
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Commands", commands)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
                return;
            case "ping":
                getJDA().getRestPing().queue( (time) ->
                        event.getChannel().sendMessageFormat("**Pong!**\nTook **%d ms**", time).queue()
                );
                return;
            case "announce":
                if (args.length < 2) {
                    EmbedManager embedManagerAnnounce = new EmbedManager("UniG0 Help", Color.RED, cmd, event.getAuthor());
                    embedManagerAnnounce.getEmbedBuilder().addField(new MessageEmbed.Field("Announcements Command Format",
                            "announce <channel> <message>", true));
                    event.getChannel().sendMessage(embedManagerAnnounce.getEmbedBuilder().build()).queue();
                    return;
                }
                String message = getRestArgs(args, 1);
                if (getChannel(args[0]) == null) {
                    EmbedManager embedManagerAnnounce = new EmbedManager("Invalid Syntax", Color.RED, cmd, event.getAuthor());
                    embedManagerAnnounce.getEmbedBuilder().addField(new MessageEmbed.Field("Announcements Command Format",
                            "announce <channel> <message>", true));
                    event.getChannel().sendMessage(embedManagerAnnounce.getEmbedBuilder().build()).queue();
                    return;
                }
                EmbedManager embedManagerAnnounce = new EmbedManager("Announcement", Color.YELLOW, "Sent by "
                        + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), null);
                embedManagerAnnounce.getEmbedBuilder().setDescription(message);
                getChannel(args[0]).sendMessage(embedManagerAnnounce.getEmbedBuilder().build()).queue();
                return;
        }
    }

    public TextChannel getChannel(final String ids) {
        String newString = ids;
        newString = newString.replace("<", "");
        newString = newString.replace(">", "");
        newString = newString.replace("#", "");
        return getJDA().getTextChannelById(newString);
    }

    public String getRestArgs(String[] args, int index) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String ignored : args) {
            if (i < index) {
                i++;
                continue;
            }
            str.append(args[i]).append(" ");
            i++;
        }
        str = new StringBuilder(str.toString().trim());
        return str.toString();
    }
}
