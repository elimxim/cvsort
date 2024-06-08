package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*
import kotlin.math.floor
import kotlin.math.sqrt

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.LINEAR,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.LINEAR,
        pseudoCode = """
        k = floor sqrt n
        buckets = [k] of List
        
        max = array[0]
        for i in (0..n) do
            if array[i] > max then
                max = array[i]
            end
        end
        
        for i in [0..n) do
            x = floor k * (array[i] / (max + 1))
            add array[i] to buckets[x]
        end
        
        idx = 0
        for i in [0..k) do
            bucket = buckets[i]
            m = length of bucket
            for j in [0..m) do
                array[idx++] = bucket[j]
            end
        end
        
        # insertion sort
        for i in [0..n) do
            v = array[i]
            j = i - 1
            while j >= 0 and array[j] > v do
                array[j+1] = array[j]
                j--
            end
            
            if i != j+1 then
                array[j+1] = v
            end
        end
        """
)
class BucketSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val bucketsSize = floor(sqrt(array.size().toDouble())).toInt()
        val buckets = Array<MutableList<Int>>(bucketsSize) { ArrayList() }

        var max = array[0]
        for (i in 1..<array.size()) {
            probe.increment(ITERATIONS, COMPARISONS)
            if (array[i] > max) {
                max = array[i]
            }
        }

        val original = array.original()
        script.ifEnabled {
            for (i in 0..<array.size()) {
                it.line(Focus(i), Extra(original.take(i + 1)))
                array[i] = 0
            }
        }

        for (i in original.indices) {
            probe.increment(ITERATIONS)
            val x = floor(bucketsSize * (original[i] / (max + 1.0))).toInt()
            buckets[x].add(original[i])
            script.ifEnabled {
                fillArray(array, buckets, false)
                val remaining = original.drop(i + 1).toIntArray() + IntArray(1)
                script.line(Extra(*remaining))
            }
        }

        fillArray(array, buckets, true)
        insertionSort(array)
    }

    private fun insertionSort(array: IntArrayWrapper) {
        for (i in 1..<array.size()) {
            probe.increment(ITERATIONS)
            val value = array[i]
            var j = i - 1
            script.line(Focus(i), Extra(value))
            val bulkMove = script.bulkMove()
            while (j >= 0 && array[j] > value) {
                probe.increment(ITERATIONS, COMPARISONS)
                array[j + 1] = array[j]
                bulkMove.add(j, j + 1)
                j--
            }

            if (bulkMove.isNotEmpty()) {
                array[j + 1] = 0
                script.line(bulkMove, Extra(value))
            }

            if (i != j + 1) {
                array[j + 1] = value
                script.line(Select(j + 1), Extra(value))
            }
        }
        script.line(Extra(0))
    }

    private fun fillArray(
            array: IntArrayWrapper,
            buckets: Array<MutableList<Int>>,
            probeEnabled: Boolean
    ) {
        var index = 0
        for (i in buckets.indices) {
            if (probeEnabled) {
                probe.increment(ITERATIONS)
            }
            val bucket = buckets[i]
            for (j in bucket.indices) {
                if (probeEnabled) {
                    probe.increment(ITERATIONS)
                }
                array[index++] = bucket[j]
            }
        }
    }
}