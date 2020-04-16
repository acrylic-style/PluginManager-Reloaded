package xyz.acrylicstyle.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ReflectionHelper;
import xyz.acrylicstyle.plugin.PluginManager;
import xyz.acrylicstyle.plugin.PluginManagerConfig;
import xyz.acrylicstyle.plugin.PluginManagerTabComplete;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginUtils {
    public static void disablePlugin(@Nullable String name) {
        disablePlugin(name == null ? null : Bukkit.getPluginManager().getPlugin(name));
    }

    public static void disablePlugin(@Nullable Plugin plugin) {
        if (plugin != null && plugin.isEnabled()) Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public static void enablePlugin(@Nullable String name) {
        enablePlugin(name == null ? null : Bukkit.getPluginManager().getPlugin(name));
    }

    public static void enablePlugin(@Nullable Plugin plugin) {
        if (plugin != null && !plugin.isEnabled()) Bukkit.getPluginManager().enablePlugin(plugin);
    }

    @NotNull
    public static String load(Plugin plugin) {
        return load(plugin == null ? null : plugin.getName());
    }

    @NotNull
    public static String load(String name) {
        if (name == null || !PluginManagerTabComplete.getPlugins().contains(name)) {
            return ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid_file");
        }
        File file = new File("./plugins/" + name + ".jar");
        if (!file.isFile()) {
            for (File f : Objects.requireNonNull(new File("./plugins/").listFiles())) {
                if (f.getName().endsWith(".jar")) {
                    try {
                        PluginDescriptionFile desc = PluginManager.getInstance().getPluginLoader().getPluginDescription(f);
                        if (desc.getName().equalsIgnoreCase(name)) {
                            file = f;
                            break;
                        }
                    } catch (InvalidDescriptionException e) {
                        return ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid_file");
                    }
                }
            }
        }
        Plugin plugin;
        try {
            plugin = Bukkit.getPluginManager().loadPlugin(file);
            plugin.onLoad();
            Bukkit.getPluginManager().enablePlugin(plugin);
        } catch (Exception e) {
            e.printStackTrace();
            return ChatColor.RED + PluginManagerConfig.getStringStatic("pman_load_invalid");
        }
        return ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_load_success"), plugin.getDescription().getName());
    }

    public static void reload(Plugin plugin) {
        if (plugin != null) {
            unload(plugin);
            load(plugin);
        }
    }

    public static void reload(String name) {
        reload(name == null ? null : Bukkit.getPluginManager().getPlugin(name));
    }

    public static String unload(String name) {
        return unload(name == null ? null : Bukkit.getPluginManager().getPlugin(name));
    }

    @SuppressWarnings({"unchecked", "RedundantCollectionOperation", "ConstantConditions"})
    public static String unload(Plugin plugin) {
        if (plugin == null) throw new NullPointerException("Plugin cannot be null");
        String name = plugin.getName();
        org.bukkit.plugin.PluginManager pluginManager = Bukkit.getPluginManager();
        SimpleCommandMap commandMap = null;
        List<Plugin> plugins = null;
        Map<String, Plugin> names = null;
        Map<String, Command> commands = null;
        Map<Event, SortedSet<RegisteredListener>> listeners = null;
        boolean reloadlisteners = true;
        if (pluginManager != null) {
            pluginManager.disablePlugin(plugin);
            try {
                plugins = (List<Plugin>) ReflectionHelper.getField(Bukkit.getPluginManager().getClass(), Bukkit.getPluginManager(), "plugins");
                names = (Map<String, Plugin>) ReflectionHelper.getField(Bukkit.getPluginManager().getClass(), Bukkit.getPluginManager(), "lookupNames");
                try {
                    listeners = (Map<Event, SortedSet<RegisteredListener>>) ReflectionHelper.getField(Bukkit.getPluginManager().getClass(), Bukkit.getPluginManager(), "listeners");
                } catch (Exception e) {
                    reloadlisteners = false;
                }
                Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);

                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                commands = (Map<String, Command>) knownCommandsField.get(commandMap);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return ChatColor.RED + String.format(PluginManagerConfig.getStringStatic("pman_unload_fail"), plugin.getName());
            }
        }

        if (plugins != null && plugins.contains(plugin)) plugins.remove(plugin);
        if (names != null && names.containsKey(name)) names.remove(name);
        if (listeners != null && reloadlisteners) for (SortedSet<RegisteredListener> set : listeners.values()) set.removeIf(value -> value.getPlugin() == plugin);
        if (commandMap != null) {
            for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Command> entry = it.next();
                if (entry.getValue() instanceof PluginCommand) {
                    PluginCommand c = (PluginCommand) entry.getValue();
                    if (c.getPlugin() == plugin) {
                        c.unregister(commandMap);
                        it.remove();
                    }
                }
            }
        }
        ClassLoader cl = plugin.getClass().getClassLoader();
        if (cl instanceof URLClassLoader) {
            try {
                ReflectionHelper.setField(cl.getClass(), cl, "plugin", null);
                ReflectionHelper.setField(cl.getClass(), cl, "pluginInit", null);
                ((URLClassLoader) cl).close();
            } catch (IOException | NoSuchFieldException | IllegalAccessException ex) {
                Logger.getLogger("PluginManager").log(Level.SEVERE, null, ex);
            }
        }
        System.gc();
        return ChatColor.GREEN + String.format(PluginManagerConfig.getStringStatic("pman_unload_success"), plugin.getName());
    }
}
