package com.github.elimxim.console

class ArrayView(
        private val array: IntArray,
        private val focus: Set<Int>,
        private val swap: Pair<Int, Int>,
) {
    fun lines(): List<String> {
        val matrix = Array(array.size) {
            Array(array.max()) { SPACE }
        }

        array.forEachIndexed { index, n ->
            val column = matrix[index]
            val cell = if (swap.first == index || swap.second == index) {
                DARK_SHADE
            } else if (focus.contains(index)) {
                MEDIUM_SHADE
            } else {
                LIGHT_SHADE
            }

            (0..<n).forEach { column[it] = cell }
        }

        return transpose(matrix).apply {
            reverse()
        }.map {
            it.joinToString(prefix = "  ", separator = "")
        }.toList()
    }

    private fun transpose(matrix: Array<Array<String>>): Array<Array<String>> {
        val rowSize = matrix.size
        val columnSize = matrix[0].size

        val transposed = Array(columnSize) {
            Array(rowSize) { SPACE }
        }

        for (i in 0..<rowSize) {
            for (j in 0..<columnSize) {
                transposed[j][i] = matrix[i][j]
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