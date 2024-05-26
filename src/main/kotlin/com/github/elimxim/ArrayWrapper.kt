package com.github.elimxim

class ArrayWrapper<T>(private val array: Array<T>,
                      private val probe: Probe) {
    operator fun get(index: Int): T {
        probe.increment(Probe.Increment.ARRAY_READS)
        return array[index]
    }

    operator fun set(index: Int, value: T) {
        probe.increment(Probe.Increment.ARRAY_WRITES)
        array[index] = value
    }

    fun swap(index1: Int, index2: Int): Boolean {
        probe.increment(Probe.Increment.ARRAY_SWAPS)
        val tmp = get(index1)
        set(index1, get(index2))
        set(index2, tmp)
        return true
    }

    fun array(): Array<T> {
        return array.copyOf()
    }

    fun size(): Int {
        return array.size
    }
}