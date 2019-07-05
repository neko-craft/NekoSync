package io.menhera.nekosync

import org.bukkit.Bukkit

object Logger {
    fun info(msg: String) {
        Bukkit.getLogger().info("[" + Main.main.name + "]: " + msg)
    }
}
