package me.dkim19375.unig0.messages;

import me.dkim19375.unig0.impl.EntryImpl;
import me.dkim19375.unig0.util.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MiscMessages extends CommandHandler {
    private final PropertiesFile file;

    public MiscMessages(JDA jda) {
        super(jda);
        file = new PropertiesFile("options.properties");
    }

    @Override
    public void onGuildMessageReceived(String cmd, String[] args, String prefix, String all, GuildMessageReceivedEvent event) {
        final Set<String> commands = new HashSet<>();
        commands.add("help");
        commands.add("prefix");
        commands.add("config");
        switch (cmd.toLowerCase()) {
            case "help":
                EmbedManager embedManager = new EmbedManager("UniG0 Help", null, Color.BLUE, cmd, null);
                embedManager.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Commands", commands)));
                event.getChannel().sendMessage(embedManager.getEmbedBuilder().build()).queue();
                return;
            case "prefix":
                if (args.length == 0) {
                    EmbedManager embedManagerPrefix = new EmbedManager("UniG0 Prefix", null, Color.BLUE, cmd, null);
                    embedManagerPrefix.getEmbedBuilder().addField("Current Prefix:", "`" + FileUtils.getPrefix(file) + "`\nYou can also use "
                            + jda.getSelfUser().getAsMention() + " as the prefix!", true);
                    event.getChannel().sendMessage(embedManagerPrefix.getEmbedBuilder().build()).queue();
                    return;
                }
                FileUtils.setPrefix(file, args[0]);
                event.getChannel().sendMessage("Successfully changed the prefix to `" + FileUtils.getPrefix(file) + "`!").queue();
                return;
            case "config":
                if (args.length == 0) {
                    final Set<String> configCommands = new HashSet<>();
                    configCommands.add("config get");
                    configCommands.add("config put");
                    configCommands.add("config load");
                    configCommands.add("config save");
                    EmbedManager embedManagerConfigHelp = new EmbedManager("UniG0 Config Help", null, Color.BLUE, cmd, null);
                    embedManagerConfigHelp.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Config Usage", configCommands)));
                    event.getChannel().sendMessage(embedManagerConfigHelp.getEmbedBuilder().build()).queue();
                    return;
                }
                switch (args[0].toLowerCase()) {
                    case "save":
                        event.getChannel().sendMessage("Saving the file...").queue();
                        if (file.saveFile()) {
                            event.getChannel().sendMessage("File successfully saved!").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Could not save file!").queue();
                        return;
                    case "load":
                        event.getChannel().sendMessage("Loading the file...").queue();
                        if (file.loadFile()) {
                            event.getChannel().sendMessage("File successfully loaded!").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Could not load file!").queue();
                        return;
                    case "put":
                        if (args.length < 3) {
                            event.getChannel().sendMessage("Not enough arguments!").queue();
                            final Set<String> configCommands = new HashSet<>();
                            configCommands.add("config put <key> <value>");
                            EmbedManager embedManagerConfigHelp = new EmbedManager("UniG0 Config Put Help", null, Color.BLUE, cmd, null);
                            embedManagerConfigHelp.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Config Put Usage", configCommands)));
                            event.getChannel().sendMessage(embedManagerConfigHelp.getEmbedBuilder().build()).queue();
                            return;
                        }
                        if (args[1].equalsIgnoreCase("token")) {
                            event.getChannel().sendMessage("Could not put value! **Reason: cannot edit `token`**").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Putting the value `" + getRestArgs(args, 2) + "` for key `" + args[1] + "`...").queue();
                        file.getProperties().put(args[1], getRestArgs(args, 2));
                        event.getChannel().sendMessage("Successfully put the value `" + getRestArgs(args, 2) + "` for key `" + args[1] + "`!").queue();
                        event.getChannel().sendMessage("Saving the file...").queue();
                        if (file.saveFile()) {
                            event.getChannel().sendMessage("File successfully saved!").queue();
                            return;
                        }
                        event.getChannel().sendMessage("Could not save file!").queue();
                        return;
                    case "get":
                        if (args.length < 2) {
                            event.getChannel().sendMessage("Not enough arguments!").queue();
                            final Set<String> configCommands = new HashSet<>();
                            configCommands.add("config get <key>");
                            EmbedManager embedManagerConfigHelp = new EmbedManager("UniG0 Config Getting Help", null, Color.BLUE, cmd, null);
                            embedManagerConfigHelp.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Config Getting Usage", configCommands)));
                            event.getChannel().sendMessage(embedManagerConfigHelp.getEmbedBuilder().build()).queue();
                            return;
                        }
                        event.getChannel().sendMessage("Getting the value for key `" + args[1] + "`...").queue();
                        if (args[1].equalsIgnoreCase("token")) {
                            event.getChannel().sendMessage("Could not get value! **Reason: not allowed to get `token`**").queue();
                            return;
                        }
                        event.getChannel().sendMessage(file.getProperties().getProperty(args[1])).queue();
                        return;
                    default:
                        event.getChannel().sendMessage("Invalid argument!").queue();
                        final Set<String> configCommands = new HashSet<>();
                        configCommands.add("config save");
                        configCommands.add("config load");
                        EmbedManager embedManagerConfigHelp = new EmbedManager("UniG0 Config Help", null, Color.BLUE, cmd, null);
                        embedManagerConfigHelp.getEmbedBuilder().addField(EmbedUtils.getEmbedGroup(new EntryImpl<>("Config Usage", configCommands)));
                        event.getChannel().sendMessage(embedManagerConfigHelp.getEmbedBuilder().build()).queue();
                        return;
                }
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
