package me.dkim19375.unig0.util.datafiles;

import me.dkim19375.unig0.UniG0;
import me.dkim19375.unig0.util.properties.GlobalProperties;

public class GlobalData {
    private String token;

    public GlobalData() {
        token = UniG0.getFileManager().getGlobalConfig().get(GlobalProperties.token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        UniG0.getFileManager().getGlobalConfig().set(GlobalProperties.token, token);
        UniG0.getFileManager().getGlobalConfig().save();
    }
}
