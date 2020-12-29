package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import util.ReflectionHelper
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.utils.PluginUtils.reload
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand
import java.lang.reflect.InvocationTargetException

@SubCommand(name = "reload", usage = "/pman reload <Plugin>", description = "Reload a plugin.")
class Reload : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.reload")) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getStringStatic("pman_reload_invalid_plugin"))
            return
        }
        try {
            reload(args[0])
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(ChatColor.RED.toString() + String.format(PluginManagerConfig.getStringStatic("pman_reload_fail"), args[0]))
            return
        }
        try {
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        } catch (ignore: ReflectiveOperationException) {}
        sender.sendMessage(ChatColor.GREEN.toString() + String.format(PluginManagerConfig.getStringStatic("pman_reload_success"), args[0]))
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