package xyz.acrylicstyle.plugin;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.tomeito_api.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_api.providers.LanguageProvider;

public class PluginManagerConfig extends ConfigProvider {
    public PluginManagerConfig(String path) {
        super(path);
    }

    @NotNull
    @Contract("_ -> new")
    public static PluginManagerConfig newInstance(@NotNull String path) {
        return new PluginManagerConfig(path);
    }

    public String getLanguage() {
        return this.getString("language", "en_US");
    }

    public static LanguageProvider getCurrentLanguageStatic() {
        return PluginManager.config.getCurrentLanguage();
    }

    public static String getStringStatic(String key) {
        return ChatColor.translateAlternateColorCodes('&', getCurrentLanguageStatic().getString(key));
    }

    public static String getStringStatic(String key, String def) {
        return ChatColor.translateAlternateColorCodes('&', getCurrentLanguageStatic().getString(key, def));
    }

    public LanguageProvider getCurrentLanguage() {
        return PluginManager.lang.get(getLanguage());
    }

    public void setLanguage(String language) {
        this.set("language", language);
    }
}
