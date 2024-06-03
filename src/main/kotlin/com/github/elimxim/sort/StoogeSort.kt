package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Increment.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.BETWEEN_QUADRATIC_AND_CUBIC,
                average = Complexity.BETWEEN_QUADRATIC_AND_CUBIC,
                best = Complexity.BETWEEN_QUADRATIC_AND_CUBIC
        ),
        spaceComplexity = Complexity.LINEAR,
        pseudoCode = """
        fun stooge(array, i = 0, j = n-1)
            if array[i] > array[j] then
                swap array[i] and array[j]
            end
            
            if j - i + 1 > 2 then
                t = (j - i + 1) / 3
                invoke stooge(array, i, j-t)
                invoke stooge(array, i+t, j)
                invoke stooge(array, i, j-t)
            end
        end
        """
)
class StoogeSort (
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        stooge(array, i = 0, j = array.size() - 1)
    }

    private fun stooge(array: IntArrayWrapper, i: Int, j: Int) {
        probe.increment(COMPARISONS)
        if (array[i] > array[j]) {
            array.swap(i, j)
            script.select(setOf(i, j))
        }

        if (j - i + 1 > 2) {
            val third = (j - i + 1) / 3
            stooge(array, i, j - third)
            stooge(array, i + third, j)
            stooge(array, i, j - third)
        }
    }
}