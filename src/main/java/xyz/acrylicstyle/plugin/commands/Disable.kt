package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import util.ReflectionHelper
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.utils.PluginUtils.disablePlugin
import xyz.acrylicstyle.plugin.utils.plus
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand
import java.lang.reflect.InvocationTargetException

@SubCommand(name = "disable", usage = "/pman disable <plugin>", description = "Disables a plugin.")
class Disable : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.disable")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_disable_invalid_plugin"))
            return
        }
        try {
            disablePlugin(args[0])
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED + java.lang.String.format(PluginManagerConfig.getStringStatic("pman_disable_fail"), args[0]))
            e.printStackTrace()
            return
        }
        sender.sendMessage(ChatColor.GREEN + java.lang.String.format(PluginManagerConfig.getStringStatic("pman_disable_success"), args[0]))
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