package com.github.elimxim

enum class SortName {
    BUBBLE,
    SELECTION,
    INSERTION,
    GNOME,
    COCKTAIL_SHAKER,
    ODD_EVEN,
    PANCAKE,
    CYCLE,
    STOOGE;

    fun canonical(): String {
        return camelCase().plus("Sort")
    }

    fun camelCase(): String {
        return name.lowercase().snakeCaseToCamelCase()
    }

    companion object {
        fun names(): Array<String> {
            return entries
                    .sortedBy { v -> v.ordinal }
                    .map { v -> v.camelCase() }
                    .toTypedArray()
        }
    }
}