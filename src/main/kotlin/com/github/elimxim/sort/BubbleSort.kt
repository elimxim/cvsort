package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Increment.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.CONST,
        pseudoCode = """
        while i in [0..n) do
            swapped = false
            while j in [1..n-i) do
                if array[j-1] > array[j] then
                    swap array[j] and array[j+1]
                    swapped = true
                end
            end
            
            if not swapped then
                break
            end
        end
        """
)
class BubbleSort(
        private val probe: Probe,
        private val scriptWriter: SortScriptWriter
) : Sort {
    override fun sort(array: ArrayWrapper<Int>) {
        for (i in 0..<array.size()) {
            probe.increment(ITERATIONS)
            var swapped = false
            for (j in 1..<array.size() - i) {
                probe.increment(ITERATIONS, COMPARISONS)
                scriptWriter.focus(array.copy(), j - 1)
                if (array[j - 1] > array[j]) {
                    swapped = array.swap(j - 1, j)
                }
            }

            if (!swapped) {
                break
            }
        }
    }
}