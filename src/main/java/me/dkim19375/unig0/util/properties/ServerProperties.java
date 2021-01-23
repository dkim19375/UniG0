package me.dkim19375.unig0.util.properties;

import me.mattstudios.config.SettingsHolder;
import me.mattstudios.config.annotations.Path;
import me.mattstudios.config.properties.Property;

public class ServerProperties implements SettingsHolder {
    @Path("prefix")
    public static final Property<String> prefix = Property.create("?");
}
