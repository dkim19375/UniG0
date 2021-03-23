package me.dkim19375.unig0.util.property

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.annotations.Path
import me.mattstudios.config.properties.Property

object GlobalProperties : SettingsHolder {
    @Path("token")
    val token = Property.create("token")
}