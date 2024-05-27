package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Increment.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.QUADRATIC
        ),
        spaceComplexity = Complexity.CONST,
        pseudoCode = """
        for i in [0..n-1) do
            min = i
            for j in (i+1..n) do
                if (arr[j] < arr[min]) then
                    min = j
                end
            end
            swap arr[i] and arr[min]
        end
        """
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
            for (j in i + 1..<n) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.focus(array, j)
                if (array[j] < array[minIdx]) {
                    minIdx = j
                }
            }
            array.swap(i, minIdx)
        }
    }
}