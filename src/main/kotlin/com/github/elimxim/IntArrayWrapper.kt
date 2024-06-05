package com.github.elimxim

// not thread safe
class IntArrayWrapper(
        private val array: IntArray,
        private val probe: Probe
) {
    operator fun get(index: Int): Int {
        probe.increment(Probe.Counter.ARRAY_READS)
        return array[index]
    }

    operator fun set(index: Int, value: Int) {
        probe.increment(Probe.Counter.ARRAY_WRITES)
        array[index] = value
    }

    fun swap(index1: Int, index2: Int): Boolean {
        probe.increment(Probe.Counter.ARRAY_SWAPS)
        val tmp = get(index1)
        set(index1, get(index2))
        set(index2, tmp)
        return true
    }

    fun original(): IntArray {
        return array.copyOf()
    }

    fun size(): Int {
        return array.size
    }
}