package xyz.acrylicstyle.plugin.utils

import org.bukkit.ChatColor

infix operator fun ChatColor.plus(s: String): String = this.toString() + s
