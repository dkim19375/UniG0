package me.dkim19375.unig0.util;

import me.dkim19375.unig0.UniG0;
import me.dkim19375.unig0.util.properties.GlobalProperties;
import me.dkim19375.unig0.util.properties.ServerProperties;
import me.mattstudios.config.SettingsManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileUtils {
    public static void reload() {
        UniG0.getFileManager().getGlobalConfig().reload();
        Map<String, SettingsManager> map = UniG0.getFileManager().getServerConfigs();
        for (SettingsManager manager : map.values()) {
            manager.reload();
        }
    }

    public static String getPrefix(String id) {
        return UniG0.getFileManager().getServerConfig(id).get(ServerProperties.prefix);
    }

    public static void setPrefix(String id, String prefix) {
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.prefix, prefix);
        UniG0.getFileManager().getServerConfig(id).save();
    }

    public static String getToken() {
        return UniG0.getFileManager().getGlobalConfig().get(GlobalProperties.token);
    }

    public static Set<String> getDeletedCommands(String id) {
        Set<String> before = new HashSet<>(UniG0.getFileManager().getServerConfig(id).get(ServerProperties.delete_commands));
        if (before.contains("*")) {
            Set<String> set = new HashSet<>();
            set.add("*");
            return set;
        }
        return before;
    }

    public static boolean shouldDeleteCommand(String id, String command) {
        return UniG0.getFileManager().getServerConfig(id).get(ServerProperties.delete_commands).contains(command);
    }

    public static void setDeletedCommands(String id, Set<String> commands) {
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.delete_commands, commands);
        UniG0.getFileManager().getServerConfig(id).save();
    }

    public static void removeDeletedCommand(String id, String command) {
        Set<String> set = new HashSet<>(getDeletedCommands(id));
        set.remove(command);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.delete_commands, set);
        UniG0.getFileManager().getServerConfig(id).save();
    }

    public static void addDeletedCommand(String id, String command) {
        Set<String> set = new HashSet<>(getDeletedCommands(id));
        set.add(command);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.delete_commands, set);
        UniG0.getFileManager().getServerConfig(id).save();
    }
}
