package com.github.elimxim.view

class ArrayView(
        private val array: IntArray,
        private val focused: Set<Int>,
        private val selected: Set<Int>,
        private val extra: IntArray,
        private val extraFocused: Set<Int>,
        private val extraSelected: Set<Int>,
        private val height: Int,
) : View {
    override fun lines(): List<String> {
        val gridWidth = array.size + GAP + extra.size

        val grid = Array(gridWidth) {
            Array(height) { SPACE }
        }

        array.forEachIndexed { i, v ->
            val column = grid[i]
            val j = column.size - 1
            (j downTo j - v + 1).forEach {
                column[it] = cell(i, selected, focused)
            }
        }

        extra.forEachIndexed { i, v ->
            val column = grid[array.size + GAP + i]
            val j = column.size - 1
            (j downTo j - v + 1).forEach {
                column[it] = cell(i, extraSelected, extraFocused)
            }
        }

        return transpose(grid).map {
            it.joinToString(prefix = SPACE, separator = "")
        }.toList()
    }

    private fun cell(index: Int, selected: Set<Int>, focused: Set<Int>): String {
        return if (selected.contains(index)) {
            DARK_SHADE
        } else if (focused.contains(index)) {
            MEDIUM_SHADE
        } else {
            LIGHT_SHADE
        }
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
        const val GAP = 2
    }
}