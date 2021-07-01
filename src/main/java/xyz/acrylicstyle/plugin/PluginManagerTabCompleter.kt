package xyz.acrylicstyle.plugin

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import xyz.acrylicstyle.plugin.utils.getPluginNames

class PluginManagerTabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        val emptyList: List<String> = ArrayList()
        if (args.isEmpty()) return commands
        if (args.size == 1) return filterArgsList(commands, args[0])
        if (args.size == 2) {
            if (args[0].equals("unload", ignoreCase = true)
                || args[0].equals("disable", ignoreCase = true)
                || args[0].equals("reload", ignoreCase = true)) return filterArgsList(allLoadedPluginNames, args[1])
            if (args[0].equals("load", ignoreCase = true)) return filterArgsList(getPluginNames().filterNot { s -> allLoadedPluginNames.contains(s) }, args[1])
            if (args[0].equals("config", ignoreCase = true)) return filterArgsList(listOf("language"), args[1])
        }
        if (args.size == 3) {
            if (args[0].equals("config", ignoreCase = true)) {
                if (args[1].equals("language", ignoreCase = true)) return filterArgsList(PluginManager.languages, args[2])
            }
        }
        return emptyList
    }

    private fun filterArgsList(list: Collection<String>, s: String): List<String> {
        return list.filter { s2 -> s2.lowercase().startsWith(s.lowercase()) }
    }

    companion object {
        var commands = listOf("load", "unload", "disable", "reload", "config")
        val allLoadedPluginNames: List<String>
            get() = Bukkit.getPluginManager().plugins
                .map { plugin -> plugin.description }
                .map { pdf -> pdf.name }
    }
}