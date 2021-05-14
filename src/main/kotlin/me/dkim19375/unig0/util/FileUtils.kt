package me.dkim19375.unig0.util

import me.dkim19375.dkim19375jdautils.util.getIgnoreCase
import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.property.GlobalProperties
import me.dkim19375.unig0.util.property.ServerProperties

@Suppress("unused")
object FileUtils {
    fun reload(bot: UniG0) {
        bot.fileManager.globalConfig.reload()
        val map = bot.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.reload()
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun save(bot: UniG0) {
        bot.fileManager.globalConfig.save()
        val map = bot.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.save()
        }
    }

    fun reset(bot: UniG0) {
        val map = bot.fileManager.getServerConfigs()
        for (manager in map.values) {
            manager.set(ServerProperties.prefix, "?")
            manager.set(ServerProperties.delete_commands, emptySet())
            manager.set(ServerProperties.disabled_channels, emptySet())
            manager.set(ServerProperties.welcomer_dm, "Welcome to {ServerName}!\nHave a great time!")
            manager.set(
                ServerProperties.welcomer_message,
                "{user} has join the server!\nWe are now at {members} members!"
            )
            manager.set(ServerProperties.welcomer_channel, "")
            manager.set(ServerProperties.welcomer_enabled_dm, true)
            manager.set(ServerProperties.welcomer_enabled_message, true)
        }
        save(bot)
    }

    fun getPrefix(id: String, bot: UniG0): String {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.prefix)
    }

    fun setPrefix(id: String, prefix: String, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.prefix, prefix)
        save(bot)
    }

    fun getToken(bot: UniG0): String = bot.fileManager.globalConfig.get(GlobalProperties.token)

    fun getDisabledCommands(id: String, bot: UniG0): Set<String> = bot.fileManager.getServerConfig(id).get(ServerProperties.disabled_commands).toSet()

    fun getDeletedCommands(id: String, bot: UniG0): Set<String> {
        val before = bot.fileManager.getServerConfig(id).get(ServerProperties.delete_commands).toSet()
        if (before.contains("*")) {
            val set = mutableSetOf<String>()
            set.add("*")
            return set
        }
        return before
    }

    fun shouldDeleteCommand(id: String, command: String, bot: UniG0): Boolean {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.delete_commands).contains(command)
    }

    fun setDeletedCommands(id: String, commands: Set<String>, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, commands)
        save(bot)
    }

    fun removeDeletedCommand(id: String, command: String, bot: UniG0) {
        val set = getDeletedCommands(id, bot).toMutableSet()
        set.remove(command)
        bot.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, set)
        save(bot)
    }

    fun addDeletedCommand(id: String, command: String, bot: UniG0) {
        val set = getDeletedCommands(id, bot).toMutableSet()
        set.add(command)
        bot.fileManager.getServerConfig(id).set(ServerProperties.delete_commands, set)
        save(bot)
    }

    fun enableCommand(id: String, command: String, bot: UniG0) {
        val set = getDisabledCommands(id, bot).toMutableSet()
        set.remove(set.getIgnoreCase(command) ?: "")
        bot.fileManager.getServerConfig(id).set(ServerProperties.disabled_commands, set)
        save(bot)
    }

    fun disableCommand(id: String, command: String, bot: UniG0) {
        val set = getDisabledCommands(id, bot).toMutableSet()
        set.remove(set.getIgnoreCase(command) ?: "")
        set.add(command)
        bot.fileManager.getServerConfig(id).set(ServerProperties.disabled_commands, set)
        save(bot)
    }

    fun getDisabledChannels(id: String, bot: UniG0): Set<String> {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.disabled_channels)
    }

    fun shouldDisableChannel(id: String, channelID: String, bot: UniG0): Boolean {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.disabled_channels).contains(channelID)
    }

    fun setDisabledChannels(id: String, channelID: Set<String>, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, channelID)
        save(bot)
    }

    fun removeDisabledChannel(id: String, channelID: String, bot: UniG0) {
        val set = getDisabledChannels(id, bot).toMutableSet()
        set.remove(channelID)
        bot.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, set)
        save(bot)
    }

    fun addDisabledChannel(id: String, channelID: String, bot: UniG0) {
        val set = getDisabledChannels(id, bot).toMutableSet()
        set.add(channelID)
        bot.fileManager.getServerConfig(id).set(ServerProperties.disabled_channels, set)
        save(bot)
    }

    fun isWelcomeMessageEnabled(id: String, bot: UniG0): Boolean {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.welcomer_enabled_message)
    }

    fun setWelcomeMessageEnabled(id: String, enabled: Boolean, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.welcomer_enabled_message, enabled)
        save(bot)
    }

    fun isWelcomeDMEnabled(id: String, bot: UniG0): Boolean {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.welcomer_enabled_dm)
    }

    fun setWelcomeDMEnabled(id: String, enabled: Boolean, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.welcomer_enabled_dm, enabled)
        save(bot)
    }

    fun getWelcomeMessage(id: String, bot: UniG0): String {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.welcomer_message)
    }

    fun setWelcomeMessage(id: String, message: String, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.welcomer_message, message)
        save(bot)
    }

    fun getDMMessage(id: String, bot: UniG0): String {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.welcomer_dm)
    }

    fun setDMMessage(id: String, message: String, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.welcomer_dm, message)
        save(bot)
    }

    fun getWelcomeChannel(id: String, bot: UniG0): String {
        return bot.fileManager.getServerConfig(id).get(ServerProperties.welcomer_channel)
    }

    fun setWelcomeChannel(id: String, channelId: String, bot: UniG0) {
        bot.fileManager.getServerConfig(id).set(ServerProperties.welcomer_channel, channelId)
        save(bot)
    }
}