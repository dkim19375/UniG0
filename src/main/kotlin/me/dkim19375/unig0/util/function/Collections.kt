package me.dkim19375.unig0.util.function

import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType

fun Collection<String>.containsIgnoreCase(other: String): Boolean {
    for (str in this) {
        if (str.equals(other, ignoreCase = true)) {
            return true
        }
    }
    return false
}

fun List<String>.getRestArgs(drop: Int): String {
    return drop(drop).joinToString(" ", transform = String::trim)
}

fun Set<Command>.getOfType(type: CommandType): Set<Command>{
    val ofType = mutableSetOf<Command>()
    for (cmd in this) {
        if (cmd.type == type) {
            ofType.add(cmd)
        }
    }
    return ofType
}

fun Set<CommandArg>.combinedArgs(): Set<String> {
    val args = mutableSetOf<String>()
    for (arg in this) {
        args.add("${arg.arg} - ${arg.description}")
    }
    return args
}

fun Set<Command>.combinedCmds(): Set<String> {
    val args = mutableSetOf<String>()
    for (arg in this) {
        args.add("${arg.command} - ${arg.description}")
    }
    return args
}

fun Set<String>.putBetween(str: String): String {
    val builder = StringBuilder()
    var first = true
    forEach { string ->
        if (!first) {
            builder.append(str)
        }
        builder.append(string)
        first = false
    }
    return builder.toString()
}

fun Set<Command>.getCommandByName(name: String): Command? {
    forEach { cmd ->
        if (cmd.name.equals(name, ignoreCase = true)) {
            return cmd
        }
        if (cmd.command.equals(name, ignoreCase = true)) {
            return cmd
        }
        if (cmd.aliases.containsIgnoreCase(name)) {
            return cmd
        }
    }
    return null
}