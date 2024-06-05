package com.github.elimxim

enum class SortSpeed(val millis: Long) {
    SNAIL(4000),
    SLOTH(2000),
    TORTOISE(1000),
    KOALA(500),
    EMU(100),
    CHEETAH(50);

    companion object {
        fun contains(name: String): Boolean {
            val v = SortSpeed.entries.find {
                it.name.equals(name, ignoreCase = true)
            }

            return v != null
        }
    }
}