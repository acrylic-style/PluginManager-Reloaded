package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import util.ReflectionHelper
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.utils.PluginUtils.unload
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand
import java.lang.reflect.InvocationTargetException

@SubCommand(name = "unload", usage = "/pman unload <Plugin>", description = "Unloads a plugin.")
class Unload : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.unload")) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getStringStatic("pman_unload_invalid_plugin"))
            return
        }
        try {
            sender.sendMessage(unload(args[0]))
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(ChatColor.RED.toString() + String.format(PluginManagerConfig.getStringStatic("pman_reload_fail"), args[0]))
        }
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