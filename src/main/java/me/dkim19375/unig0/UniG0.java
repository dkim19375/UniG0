package me.dkim19375.unig0;

import me.dkim19375.unig0.events.commands.MiscMessages;
import me.dkim19375.unig0.events.commands.SettingsCommands;
import me.dkim19375.unig0.util.FileManager;
import me.dkim19375.unig0.util.properties.GlobalProperties;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class UniG0 {
    private static FileManager fileManager;
    public static void main(String[] args) throws LoginException {
        try {
            fileManager = new FileManager();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        JDABuilder builder = JDABuilder.createDefault(fileManager.getGlobalConfig().get(GlobalProperties.token));
        builder.setActivity(Activity.watching("dkim19375 code"));
        JDA jda = builder.build();
        jda.addEventListener(new MiscMessages(jda));
        jda.addEventListener(new SettingsCommands(jda));
    }

    public static FileManager getFileManager() {
        return fileManager;
    }
}