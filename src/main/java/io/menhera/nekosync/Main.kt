package io.menhera.nekosync

import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        main = this
        saveDefaultConfig()
        ConfigManager.init()
        Database.init()
        this.server.pluginManager.registerEvents(Listener(), this)
    }

    override fun onDisable() {
        try {
            Database.conn!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        lateinit var main: Plugin
    }
}
