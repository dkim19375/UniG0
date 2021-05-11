package me.dkim19375.unig0.util.property

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property
import java.util.*

object ServerProperties : SettingsHolder {
    @Path("prefix")
    val prefix = Property.create("?")

    @Path("delete-commands")
    val delete_commands = Property.create(setOf())

    @Path("disabled-commands")
    val disabled_commands = Property.create(setOf())

    @Path("disabled-channels")
    val disabled_channels = Property.create(setOf())

    @Path("welcomer.enabled.message")
    val welcomer_enabled_message = Property.create(true)

    @Path("welcomer.enabled.dm")
    val welcomer_enabled_dm = Property.create(true)

    @Path("welcomer.message")
    val welcomer_message = Property.create("{user} has join the server!\nWe are now at {members} members!")

    @Path("welcomer.dm")
    val welcomer_dm = Property.create("Welcome to {ServerName}!\nHave a great time!")

    @Path("welcomer.channel")
    val welcomer_channel = Property.create("")
}