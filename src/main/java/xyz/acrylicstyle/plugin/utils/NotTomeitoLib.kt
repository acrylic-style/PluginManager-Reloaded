package xyz.acrylicstyle.plugin.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.Contract
import xyz.acrylicstyle.plugin.subcommand.SubCommand
import xyz.acrylicstyle.plugin.subcommand.SubCommandExecutor
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.Objects
import java.util.function.Consumer

object NotTomeitoLib {
    private val subCommands = HashMap<String, List<Map.Entry<SubCommand, SubCommandExecutor>>>()

    /**
     * Registers command with sub commands.
     * @param rootCommandName A root command name. Must be defined at plugin.yml.
     * @param classes Class list that will load. All classes must implement SubCommandExecutor or it will fail to load.
     * @param postCommand A CommandExecutor that runs very first. Return false to interrupt command execution.
     */
    fun registerCommands(rootCommandName: String, classes: List<Class<*>>, postCommand: CommandExecutor? = null) {
        val commands = ArrayList<Map.Entry<SubCommand, SubCommandExecutor>>()
        classes.forEach(Consumer { clazz: Class<*> ->
            val command = clazz.getAnnotation(
                SubCommand::class.java
            )
            try {
                val subCommandExecutor = clazz.newInstance() as SubCommandExecutor
                commands.add(SimpleImmutableEntry(command, subCommandExecutor))
                subCommands[rootCommandName] = commands
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: ClassCastException) {
                Log.warning("Couldn't cast class to SubCommandExecutor!")
                Log.warning("Class: " + clazz.canonicalName)
                Log.warning("Make sure this class implements SubCommandExecutor, then try again.")
            }
        })
        val finalPostCommand = postCommand ?: CommandExecutor { _, _, _, _ -> true }
        Objects.requireNonNull(Bukkit.getPluginCommand(rootCommandName)).executor = object : CommandExecutor {
            override fun onCommand(
                sender: CommandSender,
                command: Command,
                label: String,
                args: Array<String>,
            ): Boolean {
                if (!finalPostCommand.onCommand(sender, command, label, args)) return true
                if (args.isEmpty()) {
                    sendHelpMessage(sender)
                    return true
                }
                val shadowedCommands = subCommands[rootCommandName] ?: throw IllegalStateException("Root command isn't defined! (Tried to get $rootCommandName)")
                val entries = shadowedCommands.filter { (key) -> key.name == args[0] || key.alias == args[0] }
                if (entries.isEmpty()) {
                    sendHelpMessage(sender)
                    return true
                }
                entries
                    .map { (_, value) -> value }
                    .forEach { s -> s.onCommand(sender, args.drop(1).toTypedArray()) }
                return true
            }

            @Contract(pure = true)
            fun getCommandHelp(command: String, description: String): String {
                return ChatColor.YELLOW.toString() + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description
            }

            fun sendHelpMessage(sender: CommandSender) {
                sender.sendMessage(ChatColor.GOLD.toString() + "-----------------------------------")
                subCommands[rootCommandName]!!.map { (key) -> key }
                    .sortedBy { command -> command.name }
                    .forEach { command -> sender.sendMessage(getCommandHelp(command.usage, command.description)) }
                sender.sendMessage(ChatColor.GOLD.toString() + "-----------------------------------")
            }
        }
    }
}