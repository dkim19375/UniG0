package me.dkim19375.unig0.util;

import java.util.Properties;

public class FileUtils {
    public static String getPrefix(Properties properties) {
        return properties.getProperty("prefix", "?");
    }

    public static String getPrefix(PropertiesFile properties) {
        return properties.getProperties().getProperty("prefix", "?");
    }

    public static void setPrefix(PropertiesFile properties, String prefix) {
        properties.getProperties().put("prefix", prefix);
        properties.saveFile();
    }

    public static String getToken(PropertiesFile properties) {
        return properties.getProperties().getProperty("token", "TOKEN");
    }
}
