package xyz.acrylicstyle.plugin.commands

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import xyz.acrylicstyle.plugin.PluginManager
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.plugin.utils.plus
import xyz.acrylicstyle.tomeito_api.subcommand.OpSubCommandExecutor
import xyz.acrylicstyle.tomeito_api.subcommand.SubCommand

@SubCommand(name = "config", usage = "/pman config", description = "Changes configuration.")
class Config : OpSubCommandExecutor() {
    override fun onOpCommand(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission("pluginmanager.config")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"))
            return
        }
        if (args.isEmpty()) {
            sendMessage(sender)
            return
        }
        if (args[0].equals("language", ignoreCase = true)) {
            if (!sender.hasPermission("pluginmanager.config.language")) {
                sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"))
                return
            }
            if (!PluginManager.languages.contains(args[1])) {
                sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_config_language_invalid_language"))
                return
            }
            PluginManager.config.language = args[1]
            val lang = PluginManagerConfig.getStringStatic("language")
            val native_lang = PluginManagerConfig.getStringStatic("native_language")
            sender.sendMessage(ChatColor.GREEN + java.lang.String.format(PluginManagerConfig.getStringStatic("pman_config_language_success"), lang, native_lang))
        } else sendMessage(sender)
    }

    private fun getCommandHelp(command: String, description: String): String {
        return ChatColor.YELLOW.toString() + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description
    }

    private fun sendMessage(sender: CommandSender) {
        sender.sendMessage(ChatColor.GOLD.toString() + "------------------------------")
        sender.sendMessage(getCommandHelp("/pman config language <Language>", PluginManagerConfig.getStringStatic("pman_config_language")))
        sender.sendMessage(ChatColor.GOLD.toString() + "------------------------------")
    }
}