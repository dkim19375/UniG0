package me.dkim19375.unig0.util.datafiles;

import me.dkim19375.unig0.UniG0;
import me.dkim19375.unig0.util.properties.ServerProperties;

public class ServerData {
    private String id;
    private String prefix;

    public ServerData(String id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        prefix = UniG0.getFileManager().getServerConfig(id).get(ServerProperties.prefix);
        return prefix;
    }

    public void setPrefix(String prefix) {
        UniG0.getFileManager().getServerConfig(id).set(ServerProperties.prefix, prefix);
        this.prefix = prefix;
    }
}
