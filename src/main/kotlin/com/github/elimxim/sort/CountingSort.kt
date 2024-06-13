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
        
        out = [n]
        seq = [max+1]
        
        for i in [0..n) do
            seq[array[i]]++
        end
        
        for i in (0..max] do
            seq[i] += seq[i-1]
        end
        
        for i in (n..0] do
            seq[array[i]]--
            out[seq[array[i]]] = array[i]
        end
        
        for i in (n..0] do
            array[i] = out[i]
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1954,
                authors = ["Harold H. Seward"],
                wikiUrl = "https://en.wikipedia.org/wiki/Counting_sort"
        )
)
class CountingSort(
        private val probe: Probe,
        private val script: SortScript
): Sort {
    override fun sort(array: IntArrayWrapper) {
        val max = array.max()
        val output = IntArray(array.size())
        val sequence = IntArray(max + 1)

        for (i in 0..<array.size()) {
            probe.increment(ITERATIONS)
            sequence[array[i]]++
        }

        for (i in 1..max) {
            probe.increment(ITERATIONS)
            sequence[i] += sequence[i - 1]
        }

        for (i in array.size() - 1 downTo 0) {
            probe.increment(ITERATIONS)
            sequence[array[i]]--
            val index = sequence[array[i]]
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
    }
}