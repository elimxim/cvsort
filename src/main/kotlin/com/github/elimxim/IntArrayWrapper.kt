package com.github.elimxim

class IntArrayWrapper(
        private val array: IntArray,
        private val probe: Probe,
        private val scriptWriter: SortScript
) {
    operator fun get(index: Int): Int {
        probe.increment(Probe.Increment.ARRAY_READS)
        return array[index]
    }

    operator fun set(index: Int, value: Int) {
        probe.increment(Probe.Increment.ARRAY_WRITES)
        array[index] = value
    }

    fun swap(index1: Int, index2: Int): Boolean {
        return if (index1 != index2) {
            probe.increment(Probe.Increment.ARRAY_SWAPS)
            val tmp = get(index1)
            set(index1, get(index2))
            set(index2, tmp)
            scriptWriter.replace(this, index1, index2)
            true
        } else {
            false
        }
    }

    fun array(): IntArray {
        return array.copyOf()
    }

    fun size(): Int {
        return array.size
    }
}