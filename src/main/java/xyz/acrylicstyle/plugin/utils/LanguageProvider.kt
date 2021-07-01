package xyz.acrylicstyle.plugin.utils

import org.bukkit.ChatColor

class LanguageProvider(path: String) : ConfigProvider(path) {
    operator fun get(path: String?, def: String?): String {
        return ChatColor.translateAlternateColorCodes('&', super.getString(path, def))
    }

    override fun get(path: String): String {
        return ChatColor.translateAlternateColorCodes('&', super.getString(path))
    }

    companion object {
        fun init(plugin: String, language: String): LanguageProvider {
            return LanguageProvider("./plugins/$plugin/language_$language.yml")
        }
    }
}