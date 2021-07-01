package xyz.acrylicstyle.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import xyz.acrylicstyle.plugin.commands.*
import xyz.acrylicstyle.plugin.utils.*

class PluginManager : JavaPlugin() {
    init {
        instance = this
    }

    override fun onEnable() {
        saveResource("language_en_US.yml", true)
        lang.addLanguage("en_US")
        Bukkit.getPluginCommand("pman").tabCompleter = PluginManagerTabCompleter()
        NotTomeitoLib.registerCommands(
            "pman",
            listOf(
                ConfigCommand::class.java,
                DisableCommand::class.java,
                EnableCommand::class.java,
                LoadCommand::class.java,
                UnloadCommand::class.java,
                ReloadCommand::class.java,
            ),
        )
        // preload classes
        ReflectionHelper
        PluginUtils
        LanguageProvider
        ConfigProvider
        mutableListOf<Unit>()
        hashMapOf<Unit, Unit>()
        val kotlin = Pair(null, null)::class.java.`package`.name
        "$kotlin.text.StringsKt".tryLoadClass()
        "$kotlin.text.Regex".tryLoadClass()
        "$kotlin.jvm.internal.Intrinsics".tryLoadClass()
        "$kotlin.jvm.internal.Intrinsics.Kotlin".tryLoadClass()
        "$kotlin.jvm.internal.TypeIntrinsics".tryLoadClass()
        "$kotlin.jvm.internal.StringCompanionObject".tryLoadClass()
        "$kotlin.jvm.internal.markers.KMappedMarker".tryLoadClass()
        "$kotlin.jvm.internal.markers.KMutableList".tryLoadClass()
        "xyz.acrylicstyle.plugin.PluginManagerConfig".tryLoadClass()
        "xyz.acrylicstyle.plugin.PluginManagerConfig\$Companion".tryLoadClass()
        "xyz.acrylicstyle.plugin.utils.UtilKt".tryLoadClass()
        "xyz.acrylicstyle.plugin.utils.Log\$Logger".tryLoadClass()
        "xyz.acrylicstyle.plugin.utils.NotTomeitoLib\$NotTomeitoLib\$registerCommands\$2".tryLoadClass()
    }

    private fun String.tryLoadClass() {
        try { Class.forName(this.replace("/", ".")) } catch (ignore: ClassNotFoundException) {}
    }

    companion object {
        var lang = Lang("PluginManager")
        var config = PluginManagerConfig.newInstance("./plugins/PluginManager/config.yml")
        var languages = listOf("af_ZA", "ar_SA", "ca_ES", "cs_CZ", "da_DK", "de_DE", "el_GR", "en_US", "es_ES", "fi_FI", "fr_FR",
            "he_IL", "hu_HU", "it_IT", "ja_JP", "ko_KR", "nl_NL", "no_NO", "pl_PL", "pt_BR", "pt_PT", "ro_RO", "ru_RU", "sr_SP",
            "sv_SE", "tr_TR", "uk_UA", "vi_VN", "zh_CN", "zh_TW", "lol_US")
        lateinit var instance: PluginManager
    }
}
