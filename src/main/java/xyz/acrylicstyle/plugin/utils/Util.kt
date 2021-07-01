package xyz.acrylicstyle.plugin.utils

import org.bukkit.ChatColor
import org.bukkit.plugin.InvalidDescriptionException
import xyz.acrylicstyle.plugin.PluginManager
import java.io.File

infix operator fun ChatColor.plus(s: String): String = this.toString() + s

fun getPluginNames(): Set<String> {
    val list = HashSet<String>()
    for (f in File("./plugins/").listFiles()!!) {
        if (f.name.endsWith(".jar")) {
            try {
                list.add(PluginManager.instance.pluginLoader.getPluginDescription(f).name)
            } catch (ignore: InvalidDescriptionException) {}
        }
    }
    return list
}
