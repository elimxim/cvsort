package com.github.elimxim

import com.github.elimxim.Counter.Increment.*

class TestArray<T>(private val array: Array<T>,
                   private val counter: Counter) {

    operator fun get(index: Int): T {
        counter.increment(ARRAY_READS)
        return array[index]
    }

    operator fun set(index: Int, value: T) {
        counter.increment(ARRAY_WRITES)
        array[index] = value
    }

    fun swap(index1: Int, index2: Int): Boolean {
        counter.increment(ARRAY_SWAPS)
        val tmp = get(index1)
        set(index1, get(index2))
        set(index2, tmp)
        return true
    }

    fun size(): Int {
        return array.size
    }
}