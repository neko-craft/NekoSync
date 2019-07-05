package io.menhera.nekosync

import org.bukkit.configuration.file.FileConfiguration

object ConfigManager {
    lateinit var config: FileConfiguration
    fun init() {
        config = Main.main.config
        Logger.info("Initalize Configure Success")
    }

    fun reload() {
        Main.main.reloadConfig()
        Logger.info("Configure Reloaded")
    }
}
