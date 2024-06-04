package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Increment.*
import kotlin.math.abs

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
        script.line(Focus(0), Extra(max))
        for (i in 1..<array.size()) {
            probe.increment(ITERATIONS, COMPARISONS)
            if (array[i] > max) {
                max = array[i]
            }
            script.line(Focus(i), Extra(max))
        }

        var exp = 1
        while ((max / exp) > 0) {
            probe.increment(ITERATIONS)
            val output = array.original()
            val buckets = IntArray(10)
            val bucket = fun(i: Int): Int {
                return (array[i] / exp) % 10
            }

            for (i in 0..<array.size()) {
                probe.increment(ITERATIONS)
                buckets[bucket(i)]++
            }

            for (i in 1..<buckets.size) {
                probe.increment(ITERATIONS)
                buckets[i] += buckets[i - 1]
            }

            for (i in array.size() - 1 downTo 0) {
                probe.increment(ITERATIONS)
                script.line(Focus(i), Override(output))
                val index = buckets[bucket(i)] - 1
                output[index] = array[i]
                script.line(Move(i, index, flash = false), Override(output))
                buckets[bucket(i)]--
            }

            for (i in 0..<array.size()) {
                probe.increment(ITERATIONS)
                array[i] = output[i]
            }

            exp *= 10
        }
    }
}