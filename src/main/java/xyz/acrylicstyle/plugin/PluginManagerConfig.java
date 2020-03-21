package xyz.acrylicstyle.plugin;

import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;

import java.io.IOException;

public class PluginManagerConfig extends ConfigProvider {
    public PluginManagerConfig(String path) throws IOException, InvalidConfigurationException {
        super(path);
    }

    @NotNull
    @Contract("_ -> new")
    public static PluginManagerConfig newInstance(@NotNull String path) {
        try {
            return new PluginManagerConfig(path);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLanguage() {
        return this.getString("language", "en_US");
    }

    public void setLanguage(String language) {
        this.set("language", language);
    }
}
