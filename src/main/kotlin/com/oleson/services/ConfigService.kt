package com.oleson.services

import jakarta.inject.Singleton
import java.io.File
import java.util.Properties

@Singleton
class ConfigService {

    private val configDir = File(System.getProperty("user.home"), ".config/bibletools")
    private val configFile = File(configDir, "config")

    fun getApiKey(): String? {
        if (!configFile.exists()) return null
        val props = Properties()
        configFile.inputStream().use { props.load(it) }
        return props.getProperty("api_key")
    }

    fun setApiKey(apiKey: String) {
        configDir.mkdirs()
        val props = Properties()
        if (configFile.exists()) {
            configFile.inputStream().use { props.load(it) }
        }
        props.setProperty("api_key", apiKey)
        configFile.outputStream().use { props.store(it, "BibleTools Configuration") }
    }

    fun getConfigPath(): String = configFile.absolutePath
}
