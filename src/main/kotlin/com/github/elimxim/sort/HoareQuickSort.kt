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
        methods = [SortMethod.PARTITIONING],
        recursive = true,
        stable = false,
        pseudoCode = """
        invoke quick(array, 0, n-1)
            
        fun quick(array, l, h)
            if l >= 0 and h >= 0 and l < h then
                p = invoke partition(array, l, h)
                invoke quick(array, l, p)
                invoke quick(array, p+1, h)
            end
        end
        
        fun partition(array, l, h)
            pivot = array[l]
            i = l - 1
            j = h + 1
            
            while true do
                do i++ while array[i] < pivot
                do j-- while array[j] > pivot
                
                if i >= j then
                    return j
                end
                
                swap array[i] and array[j]
            end
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1959,
                authors = ["Tony Hoare"],
                wikiUrl = "https://en.wikipedia.org/wiki/Quicksort"
        )
)
class HoareQuickSort(
        private val probe: Probe,
        private val script: SortScript,
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        quick(array, 0, array.size() - 1)
    }

    private fun quick(array: IntArrayWrapper, low: Int, high: Int) {
        probe.increment(ITERATIONS)
        if (low >= 0 && high >= 0 && low < high) {
            val p = partition(array, low, high)
            quick(array, low, p)
            quick(array, p + 1, high)
        }
    }

    private fun partition(array: IntArrayWrapper, low: Int, high: Int): Int {
        script.action(Focus((low..high).toSet()))
        val pivot = array[low]
        script.action(Select(low), Extra(pivot))
        var i = low - 1
        var j = high + 1
        script.action(Focus(i, j), Extra(pivot))
        while (true) {
            probe.increment(ITERATIONS)
            do {
                i++
                script.action(Focus(i, j), Extra(pivot))
                probe.increment(ITERATIONS, COMPARISONS)
            } while (array[i] < pivot)

            do {
                j--
                script.action(Focus(i, j), Extra(pivot))
                probe.increment(ITERATIONS, COMPARISONS)
            } while (array[j] > pivot)

            if (i >= j) {
                return j
            }

            array.swap(i, j)
            script.action(Swap(i, j), Extra(pivot))
        }
    }
}