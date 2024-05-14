package com.github.elimxim

enum class Algorithm(val order: Int) {
    BUBBLE(1),
    SELECTION(2);

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
            return Algorithm.values()
                    .sortedBy { v -> v.order }
                    .map { v -> v.name.snakeCase() }
                    .toTypedArray()
        }
    }
}