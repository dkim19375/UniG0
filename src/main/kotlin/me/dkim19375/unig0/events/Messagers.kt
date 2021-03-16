package me.dkim19375.unig0.events;

import me.dkim19375.unig0.util.FileUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Messagers extends ListenerAdapter {
    private final JDA jda;

    public Messagers(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (FileUtils.isWelcomeDMEnabled(event.getGuild().getId())) {
            welcomeDM(event);
        }
        if (FileUtils.isWelcomeMessageEnabled(event.getGuild().getId())) {
            welcomeMessage(event);
        }
    }

    public void welcomeDM(GuildMemberJoinEvent event) {
        String message = parsePlaceholders(FileUtils.getDMMessage(event.getGuild().getId()), event);
        event.getUser().openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
    }

    public void welcomeMessage(GuildMemberJoinEvent event) {
        if (getChannel(FileUtils.getWelcomeChannel(event.getGuild().getId())) == null) {
            return;
        }
        TextChannel textChannel = getChannel(FileUtils.getWelcomeChannel(event.getGuild().getId()));
        textChannel.sendMessage(FileUtils.getWelcomeMessage(event.getGuild().getId())).queue();
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
        return jda.getTextChannelById(newString);
    }

    public String parsePlaceholders(String string, GuildMemberJoinEvent e) {
        String parsed = string;
        parsed = parsed.replaceAll("\\{ServerName}", e.getGuild().getName());
        parsed = parsed.replaceAll("\\{user}", e.getUser().getAsMention());
        parsed = parsed.replaceAll("\\{members}", String.valueOf(e.getGuild().getMembers().size()));
        return parsed;
    }
}
