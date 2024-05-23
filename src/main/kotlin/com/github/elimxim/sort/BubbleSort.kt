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
         
        """
)
class BubbleSort(private val probe: Probe) : Sort {
    override fun sort(array: TestArray<Int>) {
        for (i in 0..<array.size()) {
            probe.increment(ITERATIONS)
            var swapped = false
            for (j in 1..<array.size() - i) {
                probe.increment(ITERATIONS, COMPARISONS)
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