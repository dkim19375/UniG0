package me.dkim19375.unig0.events.messages;

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
        event.getChannel().sendMessage("Prefix sent:" + prefix).queue();
        event.getChannel().sendMessage("Actual prefix: " + FileUtils.getPrefix(event.getGuild().getId())).queue();
        if (!prefix.equalsIgnoreCase(FileUtils.getPrefix(event.getGuild().getId()))) {
            return;
        }
        commands.add("help");
        commands.add("prefix");
        switch (cmd.toLowerCase()) {
            case "help":
                EmbedManager embedManager = new EmbedManager("UniG0 Help", null, Color.BLUE, cmd, null);
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Commands", commands)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
                return;
            case "prefix":
                if (args.length == 0) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Prefix", null, Color.BLUE, cmd, null);
                    embedManagerPrefix.getEmbedBuilder().addField("Current Prefix:", "`" + FileUtils.getPrefix(event.getGuild().getId()) + "`\nYou can also use "
                            + getJDA().getSelfUser().getAsMention() + " as the prefix!", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.setPrefix(event.getGuild().getId(), args[0]);
                event.getChannel().sendMessage("Successfully changed the prefix to `" + FileUtils.getPrefix(event.getGuild().getId()) + "`!").queue();
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
