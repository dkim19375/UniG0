package me.dkim19375.unig0.util.function

import me.dkim19375.unig0.UniG0
import me.dkim19375.unig0.util.Command

fun String.getCommand(): Command? = UniG0.commands.firstOrNull { command ->
    command.aliases.plus(command.command).plus(command.name).containsIgnoreCase(this)
}