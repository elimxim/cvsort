package com.github.elimxim

enum class SortName {
    ALL,
    BUBBLE,
    SELECTION,
    INSERTION,
    GNOME,
    SHAKER,
    ODD_EVEN,
    PANCAKE,
    CYCLE,
    STOOGE,
    COMB,
    RADIX,
    SHELL,
    TREE,
    BUCKET;

    fun canonical(): String {
        return camelCase().plus("Sort")
    }

    fun camelCase(): String {
        return name.lowercase().snakeCaseToCamelCase()
    }

    companion object {
        fun realValues(): Array<SortName> {
            return entries
                    .sortedBy { it.name }
                    .filter { it != ALL }
                    .toTypedArray()
        }

        fun names(): Array<String> {
            return entries
                    .sortedBy { it.name }
                    .map { it.camelCase() }
                    .toTypedArray()
        }

        fun find(name: String, camelCase: Boolean = false, ignoreCase: Boolean = false): SortName? {
            return entries.find { sortName ->
                if (ignoreCase) {
                    if (camelCase) {
                        sortName.camelCase().equals(name, ignoreCase = true)
                    } else
                        sortName.name.equals(name, ignoreCase = true)
                } else if (camelCase) {
                    sortName.camelCase() == name.replaceFirstChar { it.titlecase() }
                } else {
                    sortName.name == name.uppercase()
                }
            }
        }

        fun determine(name: String): SortName {
            var sortName = find(name, ignoreCase = true)
            if (sortName == null) {
                sortName = find(name, camelCase = true)
            }
            return sortName!!
        }
    }
}