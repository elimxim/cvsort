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
        pseudoCode = """
        do 
            swapped = false
            
            for i in [1..n-1) step 2 do
                if array[i] > array[i+1] then
                    swap array[i] and array[i+1]
                    swapped = true
                end
            end
            
            for i in [0..n-1) step 2 do
                if array[i] > array[i+1] then
                    swap array[i] and array[i+1]
                    swapped = true
                end
            end
        while swapped
        """
)
class OddEvenSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        do {
            var swapped = false
            probe.increment(ITERATIONS)

            for (i in 1..<array.size() - 1 step 2) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.line(Focus(i))
                if (array[i] > array[i + 1]) {
                    array.swap(i, i + 1)
                    script.line(Swap(i, i + 1))
                    swapped = true
                }
            }

            for (i in 0..<array.size() - 1 step 2) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.line(Focus(i))
                if (array[i] > array[i + 1]) {
                    array.swap(i, i + 1)
                    script.line(Swap(i, i + 1))
                    swapped = true
                }
            }
        } while (swapped)
    }
}