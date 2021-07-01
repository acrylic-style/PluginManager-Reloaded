package xyz.acrylicstyle.plugin.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import org.bukkit.event.Event
import org.bukkit.plugin.InvalidDescriptionException
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener
import xyz.acrylicstyle.plugin.PluginManager
import xyz.acrylicstyle.plugin.PluginManagerConfig
import java.io.File
import java.net.URLClassLoader
import java.util.SortedSet
import java.util.logging.Level
import java.util.logging.Logger

object PluginUtils {
    fun disablePlugin(name: String?) {
        disablePlugin(if (name == null) null else Bukkit.getPluginManager().getPlugin(name))
    }

    fun disablePlugin(plugin: Plugin?) {
        if (plugin != null && plugin.isEnabled) Bukkit.getPluginManager().disablePlugin(plugin)
    }

    fun enablePlugin(name: String?) {
        enablePlugin(if (name == null) null else Bukkit.getPluginManager().getPlugin(name))
    }

    fun enablePlugin(plugin: Plugin?) {
        if (plugin != null && !plugin.isEnabled) Bukkit.getPluginManager().enablePlugin(plugin)
    }

    private fun load(plugin: Plugin?): String {
        return load(plugin?.name)
    }

    fun load(name: String?): String {
        if (name == null || name.contains("[.,/\\\\:;^~=]".toRegex())) {
            return "${ChatColor.RED}${PluginManagerConfig.getString("pman_load_invalid_file")}"
        }
        var file = File("./plugins/$name.jar")
        if (!file.exists() || !file.isFile) {
            for (f in File("./plugins/").listFiles()!!) {
                if (f.name.endsWith(".jar")) {
                    try {
                        val desc = PluginManager.instance.pluginLoader.getPluginDescription(f)
                        if (desc.name.equals(name, ignoreCase = true)) {
                            file = f
                            break
                        }
                    } catch (e: InvalidDescriptionException) {
                        return ChatColor.RED.toString() + PluginManagerConfig.getString("pman_load_invalid_file")
                    }
                }
            }
        }
        if (!file.exists() || !file.isFile) return "${ChatColor.RED}${PluginManagerConfig.getString("pman_load_invalid_file")}"
        val plugin = try {
            Bukkit.getPluginManager().loadPlugin(file).also {
                it.onLoad()
                Bukkit.getPluginManager().enablePlugin(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ChatColor.RED.toString() + PluginManagerConfig.getString("pman_load_invalid")
        }
        try {
            Bukkit.getServer().javaClass.getMethod("syncCommands").apply { isAccessible = true }.invoke(Bukkit.getServer())
        } catch (ignore: ReflectiveOperationException) {}
        return ChatColor.GREEN.toString() + java.lang.String.format(PluginManagerConfig.getString("pman_load_success"), plugin.description.name)
    }

    fun reload(plugin: Plugin?) {
        if (plugin != null) {
            unload(plugin)
            load(plugin)
        }
    }

    fun reload(name: String?) {
        reload(if (name == null) null else Bukkit.getPluginManager().getPlugin(name))
    }

    fun unload(name: String): String {
        return unload(Bukkit.getPluginManager().getPlugin(name))
    }

    @Suppress("UNCHECKED_CAST")
    fun unload(plugin: Plugin): String {
        val name = plugin.name
        val pluginManager = Bukkit.getPluginManager()
        var commandMap: SimpleCommandMap? = null
        var plugins: MutableList<Plugin?>? = null
        var names: MutableMap<String?, Plugin?>? = null
        var commands: MutableMap<String?, Command>? = null
        var listeners: Map<Event?, SortedSet<RegisteredListener>>? = null
        var reloadlisteners = true
        if (pluginManager != null) {
            pluginManager.disablePlugin(plugin)
            try {
                plugins = ReflectionHelper.getField(Bukkit.getPluginManager().javaClass, Bukkit.getPluginManager(), "plugins") as MutableList<Plugin?>
                names = ReflectionHelper.getField(Bukkit.getPluginManager().javaClass, Bukkit.getPluginManager(), "lookupNames") as MutableMap<String?, Plugin?>
                try {
                    listeners = ReflectionHelper.getField(Bukkit.getPluginManager().javaClass, Bukkit.getPluginManager(), "listeners") as Map<Event?, SortedSet<RegisteredListener>>
                } catch (e: Exception) {
                    reloadlisteners = false
                }
                val commandMapField = Bukkit.getPluginManager().javaClass.getDeclaredField("commandMap")
                commandMapField.isAccessible = true
                commandMap = commandMapField[pluginManager] as SimpleCommandMap
                val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
                knownCommandsField.isAccessible = true
                commands = knownCommandsField[commandMap] as MutableMap<String?, Command>
            } catch (e: ReflectiveOperationException) {
                e.printStackTrace()
                return ChatColor.RED.toString() + String.format(PluginManagerConfig.getString("pman_unload_fail"), plugin.name)
            }
        }
        if (plugins != null && plugins.contains(plugin)) plugins.remove(plugin)
        if (names != null && names.containsKey(name)) names.remove(name)
        if (listeners != null && reloadlisteners) for (set in listeners.values) set.removeIf { value: RegisteredListener -> value.plugin === plugin }
        if (commandMap != null) {
            val it: MutableIterator<Map.Entry<String?, Command>> = commands!!.entries.iterator()
            while (it.hasNext()) {
                val entry = it.next()
                if (entry.value is PluginCommand) {
                    val c = entry.value as PluginCommand
                    if (c.plugin === plugin) {
                        c.unregister(commandMap)
                        it.remove()
                    }
                }
            }
        }
        val cl = plugin.javaClass.classLoader
        if (cl is URLClassLoader) {
            try {
                ReflectionHelper.setField<ClassLoader>(cl.javaClass, cl, "plugin", null)
                ReflectionHelper.setField<ClassLoader>(cl.javaClass, cl, "pluginInit", null)
                cl.close()
            } catch (ex: Exception) {
                Logger.getLogger("PluginManager").log(Level.SEVERE, null, ex)
            }
        }
        System.gc()
        return ChatColor.GREEN.toString() + String.format(PluginManagerConfig.getString("pman_unload_success"), plugin.name)
    }
}