package xyz.acrylicstyle.plugin.utils

class Lang(val plugin: String) {
    /**
     * Language Map
     */
    private val languages: HashMap<String, LanguageProvider> = HashMap()

    /**
     * Add language into HashMap.
     * @param language A language that you want to define
     * @return This instance
     */
    fun addLanguage(language: String): Lang {
        languages[language] = LanguageProvider.init(plugin, language)
        return this
    }

    /**
     * Get defined LanguageProvider with specified language.
     * @throws IllegalArgumentException When specified language is not defined
     * @throws IllegalStateException When specified language was found but value is not defined
     * @param language A language that you want to get LanguageProvider
     * @return LanguageProvider that initialized by [.addLanguage]
     */
    operator fun get(language: String): LanguageProvider {
        require(languages.containsKey(language)) { "Language \"$language\" is not defined." }
        return languages[language] ?: throw IllegalStateException("Value is null")
    }
}