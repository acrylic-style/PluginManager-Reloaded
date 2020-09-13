package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import util.ReflectionHelper
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.plugins
import xyz.acrylicstyle.plugin.utils.plus
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand
import java.io.File
import java.lang.reflect.InvocationTargetException

@SubCommand(name = "load", usage = "/pman load <Plugin>", description = "Load a plugin.")
class Load : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.load")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!plugins.contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid_file"))
            return
        }
        val plugin: Plugin
        try {
            plugin = Bukkit.getPluginManager().loadPlugin(File("./plugins/" + args[0] + ".jar"))
            plugin.onLoad()
            Bukkit.getPluginManager().enablePlugin(plugin)
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid"))
            e.printStackTrace()
            return
        }
        sender.sendMessage(ChatColor.GREEN + java.lang.String.format(PluginManagerConfig.getStringStatic("pman_load_success"), plugin.description.name))
    }

    private fun sendMessage(sender: CommandSender?) {
        val executor = Bukkit.getPluginCommand("pman").executor
        try {
            ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender!!)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }
}