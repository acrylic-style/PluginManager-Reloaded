package xyz.acrylicstyle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import util.CollectionList;
import util.ICollectionList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PluginManagerTabComplete implements TabCompleter {
    public static List<String> commands = Arrays.asList("load", "unload", "disable", "reload", "config");

    public static CollectionList<String> getPlugins() {
        File itemsDir = new File("./plugins/");
        String[] itemsArray = itemsDir.list();
        if (itemsArray == null) return new CollectionList<>();
        return ICollectionList.asList(itemsArray).map(s -> s.replaceAll("\\.jar", ""));
    }

    public static CollectionList<String> getAllLoadedPluginNames() {
        return ICollectionList.asList(Bukkit.getPluginManager().getPlugins())
                .map(Plugin::getDescription)
                .map(PluginDescriptionFile::getName);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> emptyList = new ArrayList<>();
        if (args.length == 0) return commands;
        if (args.length == 1) return filterArgsList(commands, args[0]);
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("unload")
                    || args[0].equalsIgnoreCase("disable")
                    || args[0].equalsIgnoreCase("reload")) return filterArgsList(getAllLoadedPluginNames(), args[1]);
            if (args[0].equalsIgnoreCase("load")) return filterArgsList(getPlugins(), args[1]);
            if (args[0].equalsIgnoreCase("config")) return filterArgsList(Collections.singletonList("language"), args[1]);
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("config")) {
                if (args[1].equalsIgnoreCase("language")) return filterArgsList(PluginManager.languages, args[2]);
            }
        }
        return emptyList;
    }

    private CollectionList<String> filterArgsList(List<String> list, String s) { return filterArgsList(ICollectionList.asList(list), s); }

    private CollectionList<String> filterArgsList(@NotNull CollectionList<String> list, String s) {
        return list.filter(s2 -> s2.toLowerCase().replaceAll(".*:(.*)", "$1").startsWith(s.toLowerCase()));
    }
}
