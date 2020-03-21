package xyz.acrylicstyle.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.acrylicstyle.tomeito_core.TomeitoLib;
import xyz.acrylicstyle.tomeito_core.utils.Lang;

import java.io.IOException;

public class PluginManager extends JavaPlugin {
    public static Lang lang = new Lang("PluginManager");

    @Override
    public void onEnable() {
        try {
            lang.addLanguage("en_US");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        TomeitoLib.registerCommands(this.getClassLoader(), "pman", "xyz.acrylicstyle.plugin", new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if (!sender.isOp()) {
                    sender.sendMessage();
                }
                return true;
            }
        });
    }
}
