package com.github.elimxim

enum class SortSpeed(val millis: Long) {
    SLOW(1000),
    AVG(500),
    FAST(100);

    companion object {
        fun contains(name: String): Boolean {
            val v = SortSpeed.entries.find {
                it.name.equals(name, ignoreCase = true)
            }

            return v != null
        }
    }
}