package xyz.acrylicstyle.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.acrylicstyle.tomeito_api.TomeitoAPI;
import xyz.acrylicstyle.tomeito_api.utils.Lang;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PluginManager extends JavaPlugin {
    public static Lang lang = new Lang("PluginManager");
    public static PluginManagerConfig config = PluginManagerConfig.newInstance("./plugins/PluginManager/config.yml");
    public static List<String> languages = Arrays.asList("af_ZA", "ar_SA", "ca_ES", "cs_CZ", "da_DK", "de_DE", "el_GR", "en_US", "es_ES", "fi_FI", "fr_FR",
            "he_IL", "hu_HU", "it_IT", "ja_JP", "ko_KR", "nl_NL", "no_NO", "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sr_SP",
            "sv_SE", "tr_TR", "uk_UA", "vi_VN", "zh_CN", "zh_TW", "lol_US");
    private static PluginManager instance = null;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static PluginManager getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.saveResource("language_en_US.yml", true);
        try {
            lang.addLanguage("en_US");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginCommand("pman").setTabCompleter(new PluginManagerTabComplete());
        TomeitoAPI.getInstance().registerCommands(this.getClassLoader(), "pman", "xyz.acrylicstyle.plugin.commands", (sender, command, label, args) -> {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + PluginManagerConfig.getStringStatic("no_permission"));
                return false;
            }
            return true;
        });
    }
}
