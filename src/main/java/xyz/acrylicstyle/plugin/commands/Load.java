package xyz.acrylicstyle.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import util.ReflectionHelper;
import xyz.acrylicstyle.plugin.PluginManagerConfig;
import xyz.acrylicstyle.plugin.PluginManagerTabComplete;
import xyz.acrylicstyle.tomeito_core.subcommand.OpSubCommandExecutor;
import xyz.acrylicstyle.tomeito_core.subcommand.SubCommand;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

@SubCommand(name = "load", usage = "/pman load <Plugin>", description = "Load a plugin.")
public class Load extends OpSubCommandExecutor {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("pluginmanager.load")) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"));
            return;
        }
        if (args.length == 0) {
            $sendMessage(sender);
            return;
        }
        if (!PluginManagerTabComplete.getPlugins().contains(args[0])) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid_file"));
            return;
        }
        Plugin plugin;
        try {
            plugin = Bukkit.getPluginManager().loadPlugin(new File("./plugins/" + args[0] + ".jar"));
            plugin.onLoad();
            Bukkit.getPluginManager().enablePlugin(plugin);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid"));
            e.printStackTrace();
            return;
        }
        sender.sendMessage(ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_load_success"), plugin.getDescription().getName()));
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
