package io.menhera.nekosync

import org.bukkit.entity.Player

import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID

object Database {
    var conn: Connection? = null
    private lateinit var table: String
    fun init(): Boolean {
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val config = ConfigManager.config
            val auth = hashMapOf(
                    "conn" to config.getString("mysql.conn"),
                    "user" to config.getString("mysql.user"),
                    "pass" to config.getString("mysql.pass"),
                    "table" to config.getString("mysql.table")
            )
            table = auth["table"].toString()
            conn = DriverManager.getConnection(auth["conn"].toString(), auth["user"].toString(), auth["pass"].toString())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun getPlayerByUUID(uuid: UUID): Map<String, Any>? {
        val sql = String.format("SELECT * FROM %s WHERE uuid=?;", table)
        return try {
            val pst = conn!!.prepareStatement(sql)
            pst.setString(1, uuid.toString())
            val rs = pst.executeQuery()
            if (rs.next()) {
                hashMapOf(
                        "id" to rs.getInt("id"),
                        "lastip" to rs.getString("lastip"),
                        "lastlogin" to rs.getInt("lastlogin"),
                        "regip" to (rs.getString("regip") ?: "Unknown"),
                        "ban_reason" to (rs.getString("ban_reason") ?: ""),
                        "regtime" to rs.getInt("regtime"),
                        "status" to rs.getBoolean("status")
                )
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPlayer(player: Player): Map<*, *>? {
        val uuid = player.uniqueId.toString()
        val sql = String.format("SELECT * FROM %s WHERE uuid=?;", table)
        return try {
            val pst = conn!!.prepareStatement(sql)
            pst.setString(1, uuid)
            val rs = pst.executeQuery()
            if (rs.next()) {
                updateUser(player)
                return hashMapOf(
                        "id" to rs.getInt("id"),
                        "lastip" to rs.getString("lastip"),
                        "lastlogin" to rs.getInt("lastlogin"),
                        "regip" to (rs.getString("regip") ?: "Unknown"),
                        "ban_reason" to (rs.getString("ban_reason") ?: ""),
                        "regtime" to rs.getInt("regtime"),
                        "status" to rs.getBoolean("status")
                )
            } else {
                saveUser(player.uniqueId, player.name, player.address.hostName)
                getPlayer(player)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun updateUser(player: Player): Boolean {
        val sql = String.format("UPDATE %s SET lastip=?,lastlogin=?,realname=? WHERE uuid=?;", table)
        return try {
            val pst = conn!!.prepareStatement(sql)
            pst.setString(1, player.address!!.hostString)
            pst.setInt(2, Utils.timestamp())
            pst.setString(3, player.name)
            pst.setString(4, player.uniqueId.toString())
            pst.executeUpdate()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    fun saveUser(id: UUID, name: String, hostName: String): Boolean {
        val sql = String.format("insert into %s (uuid,realname,regip,lastip,regtime,lastlogin,status) VALUES (?,?,?,?,?,?,?);", table)
        return try {
            val pst = conn!!.prepareStatement(sql)
            pst.setString(1, id.toString())
            pst.setString(2, name)
            pst.setString(3, hostName)
            pst.setString(4, hostName)
            pst.setInt(5, Utils.timestamp())
            pst.setInt(6, Utils.timestamp())
            pst.setInt(7, 1)
            pst.execute()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }
}
