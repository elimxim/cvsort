package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.QUADRATIC
        ),
        spaceComplexity = Complexity.CONST,
        methods = [SortMethod.SELECTION],
        recursive = false,
        stable = false,
        pseudoCode = """
        for i in [0..n-1) do
            min = i
            for j in (i..n) do
                if (array[j] < array[min]) then
                    min = j
                end
            end
            
            if i != min then
                swap array[i] and array[min]
            end
        end
        """,
        extraInfo = ExtraInfo(
                wikiUrl = "https://en.wikipedia.org/wiki/Selection_sort"
        )
)
class SelectionSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val n = array.size()
        for (i in 0..<n - 1) {
            probe.increment(ITERATIONS)
            var minIdx = i
            script.action(Focus(i))
            for (j in i + 1..<n) {
                script.action(Focus(j))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[j] < array[minIdx]) {
                    minIdx = j
                }
            }

            if (i != minIdx) {
                array.swap(i, minIdx)
                script.action(Swap(i, minIdx))
            }
        }
    }
}