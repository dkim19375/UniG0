package me.dkim19375.unig0.util

enum class CommandType(val displayname: String) {
    OPTIONS("Options"),
    FUN("Fun"),
    LEVELS("Levels"),
    LOGGING("Logging"),
    ROLES("Roles"),
    TAGS("Tags"),
    UTILITIES("Utilities"),
    OTHER("Other");

    companion object {
        fun getByName(str: String): CommandType? {
            return try {
                valueOf(str.toUpperCase())
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}