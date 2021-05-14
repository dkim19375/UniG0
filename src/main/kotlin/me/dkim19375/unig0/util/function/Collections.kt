package me.dkim19375.unig0.util.function

import me.dkim19375.dkim19375jdautils.command.Command
import me.dkim19375.dkim19375jdautils.command.CommandArg
import me.dkim19375.dkim19375jdautils.embed.EmbedUtils
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
    EmbedUtils.getEmbedGroup(name, toList())
}