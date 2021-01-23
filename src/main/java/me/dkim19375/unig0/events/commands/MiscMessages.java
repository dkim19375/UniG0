package me.dkim19375.unig0.events.commands;

import me.dkim19375.dkim19375jdautils.embeds.EmbedManager;
import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils;
import me.dkim19375.dkim19375jdautils.impl.EntryImpl;
import me.dkim19375.unig0.util.CommandHandler;
import me.dkim19375.unig0.util.FileUtils;
import net.dv8tion.jda.api.JDA;
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
        if (!prefix.equalsIgnoreCase(FileUtils.getPrefix(event.getGuild().getId()))) {
            return;
        }
        if (FileUtils.getDeletedCommands(event.getGuild().getId()).contains(cmd)) {
            event.getMessage().delete().queue();
        }
        commands.add("help");
        commands.add("options");
        switch (cmd.toLowerCase()) {
            case "help":
                EmbedManager embedManager = new EmbedManager("UniG0 Help", null, Color.BLUE, cmd, null);
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Commands", commands)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
                return;
            case "ping":
        }
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
