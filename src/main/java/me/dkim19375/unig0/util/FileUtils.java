package me.dkim19375.unig0.util;

import me.dkim19375.unig0.UniG0;
import me.dkim19375.unig0.util.properties.GlobalProperties;
import me.dkim19375.unig0.util.properties.ServerProperties;

public class FileUtils {
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
}
