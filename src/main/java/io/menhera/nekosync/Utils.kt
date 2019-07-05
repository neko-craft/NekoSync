package io.menhera.nekosync

internal object Utils {
    fun timestamp(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
}
