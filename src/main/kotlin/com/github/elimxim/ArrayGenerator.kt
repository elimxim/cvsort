package com.github.elimxim

import kotlin.random.Random

class ArrayGenerator(private val script: SortScript = NoOpSortScript()) {
    fun generate(from: Int, to: Int, shuffle: Boolean = true): IntArray {
        val array = (from..to).toMutableList().toIntArray()
        script.discard(array.copyOf())
        if (shuffle) {
            shuffle(array)
        }
        return array
    }

    private fun shuffle(array: IntArray) {
        array.apply {
            val rnd = Random(System.currentTimeMillis())
            for (i in array.size - 1 downTo 1) {
                val j = rnd.nextInt(i + 1)
                array.swap(i, j)
                script.select(this.copyOf(), i, j)
            }
            script.discard(this.copyOf())
        }
    }
}