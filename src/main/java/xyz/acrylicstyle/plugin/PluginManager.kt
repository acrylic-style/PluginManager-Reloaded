package xyz.acrylicstyle.plugin

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import xyz.acrylicstyle.plugin.PluginManagerTabCompleter
import xyz.acrylicstyle.plugin.PluginManagerConfig
import xyz.acrylicstyle.tomeito_api.TomeitoAPI
import xyz.acrylicstyle.tomeito_api.utils.Lang
import java.util.Arrays

class PluginManager : JavaPlugin() {
    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        saveResource("language_en_US.yml", true)
        try {
            lang.addLanguage("en_US")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
        Bukkit.getPluginCommand("pman").tabCompleter = PluginManagerTabCompleter()
        TomeitoAPI.getInstance().registerCommands(classLoader, "pman", "xyz.acrylicstyle.plugin.commands") { sender: CommandSender, command: Command?, label: String?, args: Array<String?>? ->
            if (!sender.isOp) {
                sender.sendMessage(ChatColor.RED.toString() + PluginManagerConfig.getStringStatic("no_permission"))
                return@registerCommands false
            }
            true
        }
    }

    companion object {
        var lang = Lang("PluginManager")
        var config = PluginManagerConfig.newInstance("./plugins/PluginManager/config.yml")
        var languages = Arrays.asList("af_ZA", "ar_SA", "ca_ES", "cs_CZ", "da_DK", "de_DE", "el_GR", "en_US", "es_ES", "fi_FI", "fr_FR",
            "he_IL", "hu_HU", "it_IT", "ja_JP", "ko_KR", "nl_NL", "no_NO", "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sr_SP",
            "sv_SE", "tr_TR", "uk_UA", "vi_VN", "zh_CN", "zh_TW", "lol_US")
        var instance: PluginManager? = null
            private set
    }
}