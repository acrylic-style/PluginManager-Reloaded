package xyz.acrylicstyle.plugin.commands

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter.Companion.allLoadedPluginNames
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import xyz.acrylicstyle.plugin.utils.PluginUtils.disablePlugin
import xyz.acrylicstyle.plugin.utils.ReflectionHelper
import xyz.acrylicstyle.plugin.utils.plus

@SubCommand(name = "disable", usage = "/pman disable <plugin>", description = "Disables a plugin.")
class DisableCommand : SubCommandExecutor {
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.disable")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getString("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (!allLoadedPluginNames.contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getString("pman_disable_invalid_plugin"))
            return
        }
        try {
            disablePlugin(args[0])
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED + java.lang.String.format(PluginManagerConfig.getString("pman_disable_fail"), args[0]))
            e.printStackTrace()
            return
        }
        try {
            Bukkit.getServer().javaClass.getMethod("syncCommands").invoke(Bukkit.getServer())
        } catch (ignore: ReflectiveOperationException) {}
        sender.sendMessage(ChatColor.GREEN + java.lang.String.format(PluginManagerConfig.getString("pman_disable_success"), args[0]))
    }

    private fun sendMessage(sender: CommandSender) {
        val executor = Bukkit.getPluginCommand("pman").executor
        ReflectionHelper.invokeMethod(executor.javaClass, executor, "\$sendMessage", sender)
    }
}