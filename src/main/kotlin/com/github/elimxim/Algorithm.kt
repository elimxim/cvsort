package com.github.elimxim

enum class Algorithm {
    BUBBLE,
    SELECTION,
    INSERTION;

    fun canonicalName(): String {
        return name.snakeCase().plus("Sort")
    }

    companion object {
        fun contains(name: String): Boolean {
            val v = entries.find {
                it.name.equals(name, ignoreCase = true)
            }

            return v != null
        }

        fun names(): Array<String> {
            return entries
                    .sortedBy { v -> v.ordinal }
                    .map { v -> v.name.snakeCase() }
                    .toTypedArray()
        }
    }
}