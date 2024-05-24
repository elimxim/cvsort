package com.github.elimxim

class ArrayProbingWrapper(
        private val array: Array<Int>,
        private val probe: Probe
) : ArrayWrapper<Int>  {
    override fun get(index: Int): Int {
        probe.increment(Probe.Increment.ARRAY_READS)
        return array[index]
    }

    override fun set(index: Int, value: Int) {
        probe.increment(Probe.Increment.ARRAY_WRITES)
        array[index] = value
    }

    override fun swap(index1: Int, index2: Int): Boolean {
        probe.increment(Probe.Increment.ARRAY_SWAPS)
        val tmp = get(index1)
        set(index1, get(index2))
        set(index2, tmp)
        return true
    }

    override fun copy(): Array<Int> {
        return array.copyOf()
    }

    override fun size(): Int {
        return array.size
    }
}