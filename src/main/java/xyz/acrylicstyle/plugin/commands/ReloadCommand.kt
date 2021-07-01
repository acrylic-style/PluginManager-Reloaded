package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import xyz.acrylicstyle.plugin.utils.PluginUtils.reload
import xyz.acrylicstyle.plugin.utils.ReflectionHelper

@SubCommand(name = "reload", usage = "/pman reload <Plugin>", description = "Reload a plugin.")
class ReloadCommand : SubCommandExecutor {
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.reload")) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getString("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getString("pman_reload_invalid_plugin"))
            return
        }
        try {
            reload(args[0])
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(ChatColor.RED.toString() + String.format(PluginManagerConfig.getString("pman_reload_fail"), args[0]))
            return
        }
        try {
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        } catch (ignore: ReflectiveOperationException) {}
        sender.sendMessage(ChatColor.GREEN.toString() + String.format(PluginManagerConfig.getString("pman_reload_success"), args[0]))
    }

    private fun sendMessage(sender: CommandSender) {
        val executor = Bukkit.getPluginCommand("pman").executor
        ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender)
    }
}