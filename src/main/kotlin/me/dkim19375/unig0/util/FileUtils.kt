package me.dkim19375.unig0.util

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.properties.GlobalProperties
import me.dkim19375.unig0.util.properties.ServerProperties
import java.util.*

object FileUtils {
    fun reload() {
        UniG0.fileManager.globalConfig.reload()
        val map = UniG0.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.reload()
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun save() {
        UniG0.fileManager.globalConfig.save()
        val map = UniG0.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.save()
        }
    }

    fun reset() {
        val map = UniG0.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.set(ServerProperties.prefix, "?")
            manager.set(ServerProperties.delete_commands, HashSet())
            manager.set(ServerProperties.disabled_channels, HashSet())
            manager.set(ServerProperties.welcomer_dm, "Welcome to {ServerName}!\nHave a great time!")
            manager.set(
                ServerProperties.welcomer_message,
                "{user} has join the server!\nWe are now at {members} members!"
            )
            manager.set(ServerProperties.welcomer_channel, "")
            manager.set(ServerProperties.welcomer_enabled_dm, true)
            manager.set(ServerProperties.welcomer_enabled_message, true)
        }
        save()
    }

    fun getPrefix(id: String): String {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.prefix)
    }

    fun setPrefix(id: String, prefix: String) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.prefix, prefix)
        save()
    }

    val token: String
        get() = UniG0.fileManager.globalConfig.get(GlobalProperties.token)

    fun getDeletedCommands(id: String): Set<String> {
        val before: Set<String> =
            HashSet(UniG0.fileManager.getServerConfig(id).get(ServerProperties.delete_commands))
        if (before.contains("*")) {
            val set: MutableSet<String> = HashSet()
            set.add("*")
            return set
        }
        return before
    }

    fun shouldDeleteCommand(id: String, command: String): Boolean {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.delete_commands).contains(command)
    }

    fun setDeletedCommands(id: String, commands: Set<String>) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, commands)
        save()
    }

    fun removeDeletedCommand(id: String, command: String) {
        val set: MutableSet<String> = HashSet(getDeletedCommands(id))
        set.remove(command)
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, set)
        save()
    }

    fun addDeletedCommand(id: String, command: String) {
        val set: MutableSet<String> = HashSet(getDeletedCommands(id))
        set.add(command)
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, set)
        save()
    }

    fun getDisabledChannels(id: String): Set<String> {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.disabled_channels)
    }

    fun shouldDisableChannel(id: String, channelID: String): Boolean {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.disabled_channels).contains(channelID)
    }

    fun setDisabledChannels(id: String, channelID: Set<String>) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, channelID)
        save()
    }

    fun removeDisabledChannel(id: String, channelID: String) {
        val set: MutableSet<String> = HashSet(getDisabledChannels(id))
        set.remove(channelID)
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, set)
        save()
    }

    fun addDisabledChannel(id: String, channelID: String) {
        val set: MutableSet<String> = HashSet(getDisabledChannels(id))
        set.add(channelID)
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, set)
        save()
    }

    fun isWelcomeMessageEnabled(id: String): Boolean {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.welcomer_enabled_message)
    }

    fun setWelcomeMessageEnabled(id: String, enabled: Boolean) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.welcomer_enabled_message, enabled)
        save()
    }

    fun isWelcomeDMEnabled(id: String): Boolean {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.welcomer_enabled_dm)
    }

    fun setWelcomeDMEnabled(id: String, enabled: Boolean) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.welcomer_enabled_dm, enabled)
        save()
    }

    fun getWelcomeMessage(id: String): String {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.welcomer_message)
    }

    fun setWelcomeMessage(id: String, message: String) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.welcomer_message, message)
        save()
    }

    fun getDMMessage(id: String): String {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.welcomer_dm)
    }

    fun setDMMessage(id: String, message: String) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.welcomer_dm, message)
        save()
    }

    fun getWelcomeChannel(id: String): String {
        return UniG0.fileManager.getServerConfig(id).get(ServerProperties.welcomer_channel)
    }

    fun setWelcomeChannel(id: String, channelId: String) {
        UniG0.fileManager.getServerConfig(id).set(ServerProperties.welcomer_channel, channelId)
        save()
    }
}