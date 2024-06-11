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

            for (i in 1..<array.size() - 1 step 2) {
                script.action(Focus(i))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[i] > array[i + 1]) {
                    array.swap(i, i + 1)
                    script.action(Swap(i, i + 1))
                    swapped = true
                }
            }

            for (i in 0..<array.size() - 1 step 2) {
                script.action(Focus(i))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[i] > array[i + 1]) {
                    array.swap(i, i + 1)
                    script.action(Swap(i, i + 1))
                    swapped = true
                }
            }

            probe.increment(ITERATIONS)
        } while (swapped)
    }
}