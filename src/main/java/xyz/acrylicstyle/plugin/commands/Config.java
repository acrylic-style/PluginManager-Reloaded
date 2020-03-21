package xyz.acrylicstyle.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.acrylicstyle.plugin.PluginManager;
import xyz.acrylicstyle.plugin.PluginManagerConfig;
import xyz.acrylicstyle.tomeito_core.subcommand.OpSubCommandExecutor;
import xyz.acrylicstyle.tomeito_core.subcommand.SubCommand;

@SubCommand(name = "config", usage = "/pman config", description = "Changes configuration.")
public class Config extends OpSubCommandExecutor {
    @Override
    public void onOpCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("pluginmanager.config")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"));
            return;
        }
        if (args.length == 0) {
            $sendMessage(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("language")) {
            if (!sender.hasPermission("pluginmanager.config.language")) {
                sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"));
                return;
            }
            if (!PluginManager.languages.contains(args[1])) {
                sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_config_language_invalid_language"));
                return;
            }
            PluginManager.config.setLanguage(args[1]);
            String lang = PluginManagerConfig.getStringStatic("language");
            String native_lang = PluginManagerConfig.getStringStatic("native_language");
            sender.sendMessage(ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_config_language_success"), lang, native_lang));
        } else $sendMessage(sender);
    }

    public String getCommandHelp(String command, String description) {
        return ChatColor.YELLOW + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description;
    }

    public void $sendMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "------------------------------");
        sender.sendMessage(getCommandHelp("/pman config language <Language>", PluginManagerConfig.getStringStatic("pman_config_language")));
        sender.sendMessage(ChatColor.GOLD + "------------------------------");
    }
}
