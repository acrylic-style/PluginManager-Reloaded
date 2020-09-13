package xyz.acrylicstyle.plugin

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import util.CollectionList
import util.ICollectionList
import java.io.File
import java.util.ArrayList

class PluginManagerTabCompleter : TabCompleter {
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        val emptyList: List<String> = ArrayList()
        if (args.isEmpty()) return commands
        if (args.size == 1) return filterArgsList(commands, args[0])
        if (args.size == 2) {
            if (args[0].equals("unload", ignoreCase = true)
                || args[0].equals("disable", ignoreCase = true)
                || args[0].equals("reload", ignoreCase = true)) return filterArgsList(allLoadedPluginNames, args[1])
            if (args[0].equals("load", ignoreCase = true)) return filterArgsList(plugins, args[1])
            if (args[0].equals("config", ignoreCase = true)) return filterArgsList(listOf("language"), args[1])
        }
        if (args.size == 3) {
            if (args[0].equals("config", ignoreCase = true)) {
                if (args[1].equals("language", ignoreCase = true)) return filterArgsList(PluginManager.languages, args[2])
            }
        }
        return emptyList
    }

    private fun filterArgsList(list: List<String>, s: String): CollectionList<String> {
        return filterArgsList(ICollectionList.asList(list), s)
    }

    private fun filterArgsList(list: CollectionList<String>, s: String): CollectionList<String> {
        return list.filter { s2: String -> s2.toLowerCase().replace(".*:(.*)".toRegex(), "$1").startsWith(s.toLowerCase()) }
    }

    companion object {
        var commands = listOf("load", "unload", "disable", "reload", "config")
        val plugins: CollectionList<String>
            get() = ICollectionList.asList(File("./plugins/").listFiles()!!).filter { obj: File -> obj.isFile }.map { obj: File -> obj.name }.map { s: String -> s.replace("\\.jar".toRegex(), "") }
        val allLoadedPluginNames: CollectionList<String>
            get() = ICollectionList.asList(Bukkit.getPluginManager().plugins)
                .map { obj: Plugin -> obj.description }
                .map { obj: PluginDescriptionFile -> obj.name }
    }
}