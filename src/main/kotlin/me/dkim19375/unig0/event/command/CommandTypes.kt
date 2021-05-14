package me.dkim19375.unig0.event.command

import me.dkim19375.dkim19375jdautils.command.CommandType
import me.dkim19375.dkim19375jdautils.command.OTHER_TYPE

class CommandTypes {
    @Suppress("unused")
    companion object {
        @JvmField
        val FUN = object : CommandType("FUN", "Fun") {}
        @JvmField
        val LEVELS = object : CommandType("LEVELS", "Levels") {}
        @JvmField
        val LOGGING = object : CommandType("LOGGING", "Logging") {}
        @JvmField
        val ROLES = object : CommandType("ROLES", "Roles") {}
        @JvmField
        val TAGS = object : CommandType("TAGS", "Tags") {}
        @JvmField
        val UTILITIES = object : CommandType("UTILITIES", "Utilities") {}
        @JvmField
        val OTHER = OTHER_TYPE

        fun values() =
            arrayOf(
                FUN,
                LEVELS,
                LOGGING,
                ROLES,
                TAGS,
                UTILITIES,
                OTHER
            )
    }
}