package me.dkim19375.unig0.util.function

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel

fun String.getChannel(jda: JDA): TextChannel? {
    var newString = this
    newString = newString.replace("<", "")
    newString = newString.replace(">", "")
    newString = newString.replace("#", "")
    return jda.getTextChannelById(newString)
}