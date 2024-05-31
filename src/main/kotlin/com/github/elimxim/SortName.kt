package com.github.elimxim

enum class SortName {
    BUBBLE,
    SELECTION,
    INSERTION,
    GNOME,
    COCKTAIL_SHAKER,
    ODD_EVEN,
    PANCAKE;

    fun canonical(): String {
        return camelCase().plus("Sort")
    }

    fun camelCase(): String {
        return name.lowercase().snakeCaseToCamelCase()
    }

    companion object {
        fun find(name: String, camelCase: Boolean = false, ignoreCase: Boolean = false): SortName? {
            return entries.find {
                if (camelCase) {
                    it.camelCase().equals(name, ignoreCase = ignoreCase)
                } else {
                    it.name.equals(name, ignoreCase = ignoreCase)
                }
            }
        }

        fun names(): Array<String> {
            return entries
                    .sortedBy { v -> v.ordinal }
                    .map { v -> v.camelCase() }
                    .toTypedArray()
        }
    }
}