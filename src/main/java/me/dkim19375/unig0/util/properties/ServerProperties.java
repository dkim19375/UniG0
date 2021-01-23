package me.dkim19375.unig0.util.properties;

import me.mattstudios.config.SettingsHolder;
import me.mattstudios.config.annotations.Path;
import me.mattstudios.config.properties.Property;

import java.util.HashSet;
import java.util.Set;

public class ServerProperties implements SettingsHolder {
    @Path("prefix")
    public static final Property<String> prefix = Property.create("?");

    @Path("delete-commands")
    public static final Property<Set<String>> delete_commands = Property.create(new HashSet<>());
}
