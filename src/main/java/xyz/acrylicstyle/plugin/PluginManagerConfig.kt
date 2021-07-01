package xyz.acrylicstyle.plugin

import org.bukkit.ChatColor
import org.jetbrains.annotations.Contract
import xyz.acrylicstyle.plugin.utils.ConfigProvider
import xyz.acrylicstyle.plugin.utils.LanguageProvider

class PluginManagerConfig(path: String?) : ConfigProvider(path!!) {
    var language: String
        get() = this.getString("language", "en_US")
        set(language) {
            this["language"] = language
        }
    val currentLanguage: LanguageProvider
        get() = PluginManager.lang[language]

    companion object {
        @Contract("_ -> new")
        fun newInstance(path: String): PluginManagerConfig {
            return PluginManagerConfig(path)
        }

        private val currentLanguageStatic: LanguageProvider
            get() = PluginManager.config.currentLanguage

        fun getString(key: String?): String {
            return ChatColor.translateAlternateColorCodes('&', currentLanguageStatic.getString(key))
        }

        fun getString(key: String?, def: String?): String {
            return ChatColor.translateAlternateColorCodes('&', currentLanguageStatic.getString(key, def))
        }
    }
}