package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import util.ReflectionHelper
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.utils.PluginUtils.enablePlugin
import xyz.acrylicstyle.plugin.utils.plus
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand
import java.lang.reflect.InvocationTargetException

@SubCommand(name = "enable", usage = "/pman enable <plugin>", description = "Enable a plugin.")
class Enable : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.enable")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_enable_invalid_plugin"))
            return
        }
        try {
            enablePlugin(args[0])
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED + String.format(PluginManagerConfig.getStringStatic("pman_enable_fail"), args[0]))
            e.printStackTrace()
            return
        }
        sender.sendMessage(ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_enable_success"), args[0]))
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