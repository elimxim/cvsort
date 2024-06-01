package com.github.elimxim.console

import kotlin.math.max

class ArrayView(
        private val array: IntArray,
        private val variable: Int?,
        private val focused: Set<Int>,
        private val selected: Set<Int>,
) {
    fun lines(): List<String> {
        val gridWidth = array.size + 3
        val gridHeight = max(array.max(), variable ?: 0)

        val grid = Array(gridWidth) {
            Array(gridHeight) { SPACE }
        }

        array.forEachIndexed { index, v ->
            val column = grid[index]
            val cell = if (selected.contains(index)) {
                DARK_SHADE
            } else if (focused.contains(index)) {
                MEDIUM_SHADE
            } else {
                LIGHT_SHADE
            }

            (0..<v).forEach { column[it] = cell }
        }

        if (variable != null) {
            val column = grid.last()
            (0..<variable).forEach { column[it] = LIGHT_SHADE }
        }

        return transpose(grid).apply {
            reverse()
        }.map {
            it.joinToString(prefix = "  ", separator = "")
        }.toList()
    }

    private fun transpose(grid: Array<Array<String>>): Array<Array<String>> {
        val rowSize = grid.size
        val columnSize = grid[0].size

        val transposed = Array(columnSize) {
            Array(rowSize) { SPACE }
        }

        for (i in 0..<rowSize) {
            for (j in 0..<columnSize) {
                transposed[j][i] = grid[i][j]
            }
        }

        return transposed
    }

    private companion object {
        const val SPACE = "\u0020\u0020"
        const val LIGHT_SHADE = "\u2591\u2591"
        const val MEDIUM_SHADE = "\u2592\u2592"
        const val DARK_SHADE = "\u2593\u2593"
    }
}