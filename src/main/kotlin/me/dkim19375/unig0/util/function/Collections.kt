package me.dkim19375.unig0.util.function

import me.dkim19375.dkim19375jdautils.embeds.EmbedUtils
import me.dkim19375.dkim19375jdautils.impl.EntryImpl
import me.dkim19375.unig0.util.Command
import me.dkim19375.unig0.util.CommandArg
import me.dkim19375.unig0.util.CommandType
import net.dv8tion.jda.api.entities.MessageEmbed
import java.util.*

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

fun Set<Command>.getOfType(type: CommandType): Set<Command> {
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

fun <T> Set<T?>?.filterNonNull(): Set<T> = this?.filter(Objects::nonNull)?.map { t -> t!! }?.toSet() ?: setOf()

fun <T> List<T?>?.filterNonNull(): List<T> = this?.filter(Objects::nonNull)?.map { t -> t!! } ?: listOf()

fun Collection<String>?.getEmbedField(
    name: String,
    noValue: String = "None",
    inline: Boolean = false
): MessageEmbed.Field = if (isNullOrEmpty()) {
    MessageEmbed.Field(name, noValue, inline)
} else {
    EmbedUtils.getEmbedGroup(EntryImpl(name, toMutableList()))
}

fun Iterable<String>.containsIgnoreCase(find: String): Boolean = getIgnoreCase(find) != null

fun Iterable<String>.getIgnoreCase(find: String): String? = firstOrNull { it.equals(find, ignoreCase = true) }

fun Set<Command>.getCommandByName(name: String): Command? {
    return firstOrNull { cmd ->
        cmd.name.equals(name, ignoreCase = true)
                || cmd.command.equals(name, ignoreCase = true)
                || cmd.aliases.containsIgnoreCase(name)
    }
}