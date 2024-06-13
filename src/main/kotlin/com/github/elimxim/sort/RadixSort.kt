package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.LINEAR,
                average = Complexity.LINEAR,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.LINEAR,
        methods = [],
        recursive = false,
        stable = true,
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
            seq = [10]
            
            for i in [0..n) do
                x = (array[i] / exp) % 10
                seq[x]++
            end
            
            for i in [1..10) do
                seq[i] += seq[i-1]
            end
            
            for i in (n..0] do
                x = (array[i] / exp) % 10
                seq[x]--
                out[seq[x]] = array[i]
            end
            
            for i in (n..0] do
                array[i] = out[i]
            end
            
            exp *= 10
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1954,
                authors = ["Harold H. Seward"],
                wikiUrl = "https://en.wikipedia.org/wiki/Radix_sort"
        )
)
class RadixSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val max = array.max()
        var exp = 1
        while ((max / exp) > 0) {
            probe.increment(ITERATIONS)
            val output = IntArray(array.size())
            val sequence = IntArray(10)
            val x = fun(i: Int): Int {
                return (array[i] / exp) % 10
            }

            for (i in 0..<array.size()) {
                probe.increment(ITERATIONS)
                sequence[x(i)]++
            }

            for (i in 1..<sequence.size) {
                probe.increment(ITERATIONS)
                sequence[i] += sequence[i - 1]
            }

            for (i in array.size() - 1 downTo 0) {
                probe.increment(ITERATIONS)
                sequence[x(i)]--
                val index = sequence[x(i)]
                output[index] = array[i]
                script.action(Focus(i), Extra(output, Select(index)))

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