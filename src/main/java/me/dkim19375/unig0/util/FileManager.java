package me.dkim19375.unig0.util;

import me.dkim19375.unig0.util.properties.GlobalProperties;
import me.dkim19375.unig0.util.properties.ServerProperties;
import me.mattstudios.config.SettingsManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private SettingsManager globalConfig;
    private Map<String, SettingsManager> serverConfigs = new HashMap<>();

    public FileManager() throws IOException {
        File file = Paths.get("data", "global-config.yml").toFile();
        file.getParentFile().mkdir();
        file.createNewFile();
        globalConfig = SettingsManager.from(file).configurationData(GlobalProperties.class).create();
    }

    public FileManager(boolean dontThrowException) {
        File file = Paths.get("data", "global-config.yml").toFile();
        try {
            file.getParentFile().mkdir();
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        globalConfig = SettingsManager.from(file).configurationData(GlobalProperties.class).create();
    }

    public void addServerId(String id) throws IOException {
        if (serverConfigs.containsKey(id)) {
            return;
        }
        File file = Paths.get("data", "servers", id + ".yml").toFile();
        file.getParentFile().mkdir();
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        SettingsManager manager = SettingsManager.from(file).configurationData(ServerProperties.class).create();
        serverConfigs.put(id, manager);
        manager.save();
    }

    public SettingsManager getServerConfig(String id) {
        if (serverConfigs.containsKey(id)) {
            return serverConfigs.get(id);
        }
        try {
            addServerId(id);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
        return serverConfigs.get(id);
    }

    public SettingsManager getGlobalConfig() {
        return globalConfig;
    }

    public Map<String, SettingsManager> getServerConfigs() {
        return serverConfigs;
    }
}
