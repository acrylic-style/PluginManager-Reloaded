package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import xyz.acrylicstyle.plugin.utils.PluginUtils
import xyz.acrylicstyle.plugin.utils.ReflectionHelper
import xyz.acrylicstyle.plugin.utils.plus

@SubCommand(name = "load", usage = "/pman load <Plugin>", description = "Load a plugin.")
class LoadCommand : SubCommandExecutor {
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.load")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getString("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        sender.sendMessage(PluginUtils.load(args[0]))
    }

    private fun sendMessage(sender: CommandSender) {
        val executor = Bukkit.getPluginCommand("pman").executor
        ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender)
    }
}