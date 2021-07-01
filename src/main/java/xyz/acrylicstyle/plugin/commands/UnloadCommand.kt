package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import xyz.acrylicstyle.plugin.utils.PluginUtils.unload
import xyz.acrylicstyle.plugin.utils.ReflectionHelper

@SubCommand(name = "unload", usage = "/pman unload <Plugin>", description = "Unloads a plugin.")
class UnloadCommand : SubCommandExecutor {
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.unload")) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getString("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getString("pman_unload_invalid_plugin"))
            return
        }
        try {
            if (args[0].equals("PluginManager", true)) sender.sendMessage("${ChatColor.YELLOW}Warning: You are unloading the PluginManager itself.")
            sender.sendMessage(unload(args[0]))
            try {
                Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
            } catch (ignore: ReflectiveOperationException) {}
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(ChatColor.RED.toString() + String.format(PluginManagerConfig.getString("pman_reload_fail"), args[0]))
        }
    }

    private fun sendMessage(sender: CommandSender) {
        val executor = Bukkit.getPluginCommand("pman").executor
        ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender)
    }
}