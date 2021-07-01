package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import xyz.acrylicstyle.plugin.utils.PluginUtils.enablePlugin
import xyz.acrylicstyle.plugin.utils.ReflectionHelper
import xyz.acrylicstyle.plugin.utils.plus

@SubCommand(name = "enable", usage = "/pman enable <plugin>", description = "Enable a plugin.")
class EnableCommand : SubCommandExecutor {
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.enable")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getString("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getString("pman_enable_invalid_plugin"))
            return
        }
        try {
            enablePlugin(args[0])
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED + String.format(PluginManagerConfig.getString("pman_enable_fail"), args[0]))
            e.printStackTrace()
            return
        }
        try {
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        } catch (ignore: ReflectiveOperationException) {}
        sender.sendMessage(ChatColor.GREEN + String.format(PluginManagerConfig.getString("pman_enable_success"), args[0]))
    }

    private fun sendMessage(sender: CommandSender) {
        val executor = Bukkit.getPluginCommand("pman").executor
        ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender)
    }
}