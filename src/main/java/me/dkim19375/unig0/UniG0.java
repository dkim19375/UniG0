package me.dkim19375.unig0;

import me.dkim19375.unig0.messages.MiscMessages;
import me.dkim19375.unig0.util.FileUtils;
import me.dkim19375.unig0.util.PropertiesFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class UniG0 {
    public static void main(String[] args) throws LoginException {
        PropertiesFile file = new PropertiesFile("options.properties");
        file.createFile(true);
        JDABuilder builder = JDABuilder.createDefault(file.getProperties().getProperty("token", "TOKEN"));
        builder.setActivity(Activity.watching("dkim19375 code"));
        JDA jda = builder.build();
        jda.addEventListener(new MiscMessages(jda, file));
        FileUtils.getPrefix(file);
    }
}