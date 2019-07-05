package io.menhera.nekosync

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent

class Listener : Listener {
    @EventHandler
    fun onPlayerPerJoin(event: AsyncPlayerPreLoginEvent) {
        val playerData = Database.getPlayerByUUID(event.uniqueId)
        if (playerData == null) {
            event.run {
                Database.saveUser(uniqueId, name, address.hostName)
            }
        }
        if (playerData!!["status"] == false) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, playerData["ban_reason"].toString())
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        println(Database.getPlayer(player))
    }
}
