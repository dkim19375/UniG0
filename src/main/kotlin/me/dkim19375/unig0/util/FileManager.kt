package me.dkim19375.unig0.util

import me.dkim19375.unig0.util.property.GlobalProperties
import me.dkim19375.unig0.util.property.ServerProperties
import me.mattstudios.config.SettingsManager
import java.nio.file.Paths
import java.util.*

class FileManager {
    var globalConfig: SettingsManager
        private set
    private val serverConfigs: MutableMap<String, SettingsManager> = HashMap()

    init {
        val file = Paths.get("data", "global-config.yml").toFile()
        file.parentFile.mkdir()
        file.createNewFile()
        globalConfig = SettingsManager.from(file).configurationData(GlobalProperties::class.java).create()
    }

    private fun addServerId(id: String) {
        if (serverConfigs.containsKey(id)) {
            return
        }
        val file = Paths.get("data", "servers", "$id.yml").toFile()
        file.parentFile.mkdir()
        file.createNewFile()
        val manager = SettingsManager.from(file).configurationData(
            ServerProperties::class.java
        ).create()
        serverConfigs[id] = manager
        manager.save()
    }

    fun getServerConfig(id: String): SettingsManager {
        serverConfigs[id]?.let { return it }
        addServerId(id)
        return serverConfigs[id]!!
    }

    fun getServerConfigs(): Map<String, SettingsManager> {
        return serverConfigs
    }
}