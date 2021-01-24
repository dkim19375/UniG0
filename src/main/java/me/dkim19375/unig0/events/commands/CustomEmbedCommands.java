package me.dkim19375.unig0.events.commands;

import me.dkim19375.unig0.util.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CustomEmbedCommands extends CommandHandler {
    public CustomEmbedCommands(JDA jda) {
        super(jda);
    }

    @Override
    public void onGuildMessageReceived(String cmd, String[] args, String prefix, String all, GuildMessageReceivedEvent event) {
        if (!cmd.equalsIgnoreCase("embed")) {
            return;
        }
        if (args.length < 1) {

            return;
        }
    }
}
