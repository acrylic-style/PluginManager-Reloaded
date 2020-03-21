package xyz.acrylicstyle.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import util.ReflectionHelper;
import xyz.acrylicstyle.plugin.PluginManagerConfig;
import xyz.acrylicstyle.plugin.PluginManagerTabComplete;
import xyz.acrylicstyle.plugin.utils.PluginUtils;
import xyz.acrylicstyle.tomeito_core.command.OpCommandExecutor;
import xyz.acrylicstyle.tomeito_core.subcommand.SubCommand;

import java.lang.reflect.InvocationTargetException;

@SubCommand(name = "reload", usage = "/pman reload <Plugin>", description = "Reload a plugin.")
public class Reload extends OpCommandExecutor {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("pluginmanager.reload")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"));
            return;
        }
        if (args.length == 0) {
            $sendMessage(sender);
            return;
        }
        if (!PluginManagerTabComplete.getAllLoadedPluginNames().contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_reload_invalid_plugin"));
            return;
        }
        try {
            PluginUtils.reload(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + String.format(PluginManagerConfig.getStringStatic("pman_reload_fail"), args[0]));
            return;
        }
        sender.sendMessage(ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_reload_success"), args[0]));
    }

    public void $sendMessage(CommandSender sender) {
        CommandExecutor executor = Bukkit.getPluginCommand("pman").getExecutor();
        try {
            ReflectionHelper.invokeMethod(executor.getClass(), executor, "$sendMessage", sender);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}