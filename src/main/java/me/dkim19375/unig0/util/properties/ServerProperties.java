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

    @Path("disabled-channels")
    public static final Property<Set<String>> disabled_channels = Property.create(new HashSet<>());

    @Path("welcomer.enabled.message")
    public static final Property<Boolean> welcomer_enabled_message = Property.create(true);

    @Path("welcomer.enabled.dm")
    public static final Property<Boolean> welcomer_enabled_dm = Property.create(true);

    @Path("welcomer.message")
    public static final Property<String> welcomer_message = Property.create("{user} has join the server!\nWe are now at {members} members!");

    @Path("welcomer.dm")
    public static final Property<String> welcomer_dm = Property.create("Welcome to {ServerName}!\nHave a great time!");

    @Path("welcomer.channel")
    public static final Property<String> welcomer_channel = Property.create("");
}
