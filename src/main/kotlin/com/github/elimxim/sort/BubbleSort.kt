package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.CONST,
        methods = [Method.EXCHANGING],
        stable = true,
        pseudoCode = """
        for i in [0..n) do
            swapped = false
            for j in [1..n-i) do
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
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val n = array.size()
        for (i in 0..<n) {
            probe.increment(ITERATIONS)
            var swapped = false
            for (j in 1..<n - i) {
                script.action(Focus(j - 1))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[j - 1] > array[j]) {
                    swapped = array.swap(j - 1, j)
                    script.action(Swap(j - 1, j))
                }
            }

            if (!swapped) {
                break
            }
        }
    }
}