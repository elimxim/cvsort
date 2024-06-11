package com.github.elimxim

import com.github.elimxim.Probe.Counter.*

class IntArrayWrapper(
        private val array: IntArray,
        private val probe: Probe
) {
    operator fun get(index: Int): Int {
        probe.increment(ARRAY_READS)
        return array[index]
    }

    operator fun set(index: Int, value: Int) {
        probe.increment(ARRAY_WRITES)
        array[index] = value
    }

    fun swap(index1: Int, index2: Int): Boolean {
        probe.increment(ARRAY_SWAPS)
        val tmp = get(index1)
        set(index1, get(index2))
        set(index2, tmp)
        return true
    }

    fun max(): Int {
        var max = get(0)
        for (i in 1..<size()) {
            probe.increment(ITERATIONS, COMPARISONS)
            if (get(i) > max) {
                max = get(i)
            }
        }
        return max
    }

    fun snapshot(): IntArray {
        return array.copyOf()
    }

    fun size(): Int {
        return array.size
    }
}