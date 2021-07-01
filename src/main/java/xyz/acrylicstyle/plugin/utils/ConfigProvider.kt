package xyz.acrylicstyle.plugin.utils

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

open class ConfigProvider @JvmOverloads constructor(path: String, disableConstructor: Boolean = false) : YamlConfiguration() {
    private val file: File
    private val path: String

    constructor(file: File) : this(file.absolutePath)

    fun save() {
        try {
            this.save(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * @param path relative or absolute path from the spigot.jar
         * @return ConfigProvider
         */
        private fun initWithoutException(path: String): ConfigProvider? {
            return try {
                ConfigProvider(path)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        /**
         * @param file An configuration file
         * @return ConfigProvider
         */
        private fun initWithoutException(file: File): ConfigProvider? {
            return try {
                ConfigProvider(file)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun getConfig(file: File): ConfigProvider {
            return initWithoutException(file)
                ?: throw NullPointerException()
        }

        fun getBoolean(path: String?, def: Boolean?, pluginName: String): Boolean {
            return getBoolean(path, def, File("./plugins/$pluginName/config.yml"))
        }

        fun getBoolean(path: String?, def: Boolean?, file: File): Boolean {
            return getConfig(file).getBoolean(path, def!!)
        }

        fun getString(path: String?, def: String?, pluginName: String): String {
            return getString(path, def, File("./plugins/$pluginName/config.yml"))
        }

        fun getString(path: String?, def: String?, file: File): String {
            return getConfig(file).getString(path, def)
        }
    }

    init {
        if (disableConstructor) throw UnsupportedOperationException()
        this.path = path
        file = File(this.path)
        // avoid some dangerous situation (e.g. there is a file or directory and accidentally deletes it)
        if (!file.exists()) {
            file.mkdirs() // creates directory(ies) including file name
            file.delete() // deletes file/directory but not parent directory
            try {
                file.createNewFile() // then finally create a file
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        this.load(file)
    }
}