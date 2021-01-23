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

    public static void save() {
        UniG0.getFileManager().getGlobalConfig().save();
        Map<String, SettingsManager> map = UniG0.getFileManager().getServerConfigs();
        for (SettingsManager manager : map.values()) {
            manager.save();
        }
    }

    public static String getPrefix(String id) {
        return UniG0.getFileManager().getServerConfig(id).get(ServerProperties.prefix);
    }

    public static void setPrefix(String id, String prefix) {
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.prefix, prefix);
        reload();
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
        save();
    }

    public static void removeDeletedCommand(String id, String command) {
        Set<String> set = new HashSet<>(getDeletedCommands(id));
        set.remove(command);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.delete_commands, set);
        save();
    }

    public static void addDeletedCommand(String id, String command) {
        Set<String> set = new HashSet<>(getDeletedCommands(id));
        set.add(command);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.delete_commands, set);
        save();
    }

    public static Set<String> getDisabledChannels(String id) {
        return UniG0.getFileManager().getServerConfig(id).get(ServerProperties.disabled_channels);
    }

    public static boolean shouldDisableChannel(String id, String channelID) {
        return UniG0.getFileManager().getServerConfig(id).get(ServerProperties.disabled_channels).contains(channelID);
    }

    public static void setDisabledChannels(String id, Set<String> channelID) {
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.disabled_channels, channelID);
        save();
    }

    public static void removeDisabledChannel(String id, String channelID) {
        Set<String> set = new HashSet<>(getDisabledChannels(id));
        set.remove(channelID);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.disabled_channels, set);
        save();
    }

    public static void addDisabledChannel(String id, String channelID) {
        Set<String> set = new HashSet<>(getDisabledChannels(id));
        set.add(channelID);
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.disabled_channels, set);
        save();
    }
}
