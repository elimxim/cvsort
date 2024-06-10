package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.LINEAR,
                average = Complexity.LINEAR,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.LINEAR,
        pseudoCode = """
        max = array[0]
        for i in (0..n) do
            if array[i] > max then
                max = array[i]
            end
        end
        
        exp = 1
        while max/exp > 0 do
            out = [n]
            occ = [10]
            
            for i in [0..n) do
                x = (array[i] / exp) % 10
                occ[x]++
            end
            
            for i in [1..10) do
                occ[i] += occ[i-1]
            end
            
            for i in (n..0] do
                x = (array[i] / exp) % 10
                out[occ[x]-1] = array[i]
                occ[x]--
            end
            
            for i in [0..n) do
                array[i] = out[i]
            end
            
            exp *= 10
        end
        """
)
class RadixSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        var max = array[0]
        for (i in 1..<array.size()) {
            probe.increment(ITERATIONS, COMPARISONS)
            if (array[i] > max) {
                max = array[i]
            }
        }

        var exp = 1
        while ((max / exp) > 0) {
            probe.increment(ITERATIONS)
            val output = IntArray(array.size())
            val buckets = IntArray(10)
            val x = fun(i: Int): Int {
                return (array[i] / exp) % 10
            }

            for (i in 0..<array.size()) {
                probe.increment(ITERATIONS)
                buckets[x(i)]++
            }

            for (i in 1..<buckets.size) {
                probe.increment(ITERATIONS)
                buckets[i] += buckets[i - 1]
            }

            for (i in array.size() - 1 downTo 0) {
                probe.increment(ITERATIONS)
                val index = buckets[x(i)] - 1
                output[index] = array[i]
                script.action(Focus(i), Extra(output, Select(index)))
                buckets[x(i)]--
            }

            for (i in array.size() - 1 downTo 0) {
                probe.increment(ITERATIONS)
                array[i] = output[i]
                script.scene {
                    it.action(Select(i), Extra(output, Focus(i)))
                    output[i] = 0
                }
            }

            exp *= 10
        }
    }
}