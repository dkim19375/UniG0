package me.dkim19375.unig0.events.messages;

import me.dkim19375.unig0.util.CommandHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SettingsCommands extends CommandHandler {
    public SettingsCommands(JDA jda) {
        super(jda);
    }

    @Override
    public void onGuildMessageReceived(String cmd, String[] args, String prefix, String all, GuildMessageReceivedEvent event) {
        if (!cmd.equalsIgnoreCase("options")) {
            return;
        }

    }
}
