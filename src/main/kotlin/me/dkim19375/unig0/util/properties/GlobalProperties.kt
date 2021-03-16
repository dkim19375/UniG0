package me.dkim19375.unig0.util.properties;

import me.mattstudios.config.SettingsHolder;
import me.mattstudios.config.annotations.Path;
import me.mattstudios.config.properties.Property;

public class GlobalProperties implements SettingsHolder {
    @Path("token")
    public static final Property<String> token = Property.create("token");
}
