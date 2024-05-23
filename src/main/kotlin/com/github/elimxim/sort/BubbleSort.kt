package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Counter.Increment.*

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
class BubbleSort(private val counter: Counter) : Sort {
    override fun sort(array: TestArray<Int>) {
        for (i in 0..<array.size()) {
            counter.increment(ITERATIONS)
            var swapped = false
            for (j in 1..<array.size() - i) {
                counter.increment(ITERATIONS, COMPARISONS)
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