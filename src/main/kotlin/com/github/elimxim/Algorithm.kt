package com.github.elimxim

enum class Algorithm {
    BUBBLE,
    SELECTION,
    INSERTION,
    GNOME,
    COCKTAIL_SHAKER;

    fun canonicalName(): String {
        return name.lowercase().snakeCaseToCamelCase().plus("Sort")
    }

    companion object {
        fun contains(name: String): Boolean {
            val e = entries.find {
                it.name.snakeCaseToCamelCase().equals(name, ignoreCase = true)
            }

            return e != null
        }

        fun names(): Array<String> {
            return entries
                    .sortedBy { v -> v.ordinal }
                    .map { v -> v.name.lowercase().snakeCaseToCamelCase() }
                    .toTypedArray()
        }
    }
}