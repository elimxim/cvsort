package com.github.elimxim

import kotlin.random.Random

object ArrayGenerator {
    fun generate(from: Int, to: Int): Array<Int> {
        val array = (from..to).toMutableList().toTypedArray()
        array.apply {
            val rnd = Random(System.currentTimeMillis())
            for (i in array.size downTo 2) {
                val j = rnd.nextInt(i)

                val tmp = array[i - 1]
                array[i - 1] = array[j]
                array[j] = tmp
            }
        }
        return array
    }
}