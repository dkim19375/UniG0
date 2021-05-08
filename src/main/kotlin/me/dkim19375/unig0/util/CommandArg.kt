package me.dkim19375.unig0.util

import net.dv8tion.jda.api.Permission

data class CommandArg(val baseCommand: Command, val arg: String, val description: String, val permissions: Set<Permission> = setOf())