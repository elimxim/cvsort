package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.LINEARITHMIC,
                best = Complexity.LINEARITHMIC,
        ),
        spaceComplexity = Complexity.LINEAR,
        methods = [Method.PARTITIONING],
        recursive = true,
        stable = false,
        pseudoCode = """
        invoke quick(array, 0, n-1)
        
        fun quick(array, l, h)
            if l < h then
                p = invoke partition(array, l, h)
                invoke quick(array, l, p-1)
                invoke quick(array, p+1, h)
            end
        end
        
        fun partition(array, l, h)
            pivot = array[h]
            i = l
            for j in [l..h) do
                if array[j] <= pivot then
                    if i != j then
                        swap array[i] and array[j]
                    end
                    i++
                end
            end
            
            swap array[i] and array[h]
            return i
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1986,
                authors = ["Jon Bentley"],
                wikiUrl = "https://en.wikipedia.org/wiki/Quicksort"
        )
)
class LomutoQuickSort(
        private val probe: Probe,
        private val script: SortScript,
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        quick(array, 0, array.size() - 1)
    }

    private fun quick(array: IntArrayWrapper, low: Int, high: Int) {
        probe.increment(ITERATIONS)
        if (low < high) {
            val p = partition(array, low, high)
            quick(array, low, p - 1)
            quick(array, p + 1, high)
        }
    }

    private fun partition(array: IntArrayWrapper, low: Int, high: Int): Int {
        script.action(Focus((low..high).toSet()))
        val pivot = array[high]
        var i = low
        for (j in low..<high) {
            script.action(Focus(j), Extra(pivot))
            probe.increment(ITERATIONS, COMPARISONS)
            if (array[j] <= pivot) {
                if (i != j) {
                    array.swap(i, j)
                    script.action(Swap(i, j), Extra(pivot))
                }
                i++
            }
        }

        array.swap(i, high)
        script.action((Swap(i, high)))
        return i
    }
}