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
        for i in (0..n) do
            v = array[i]
            j = i - 1
            while j >= 0 and array[j] > v do
                array[j+1] = array[j]
                j--
            end
            
            if i != j+1 then
                array[j+1] = v
            end
        end
        """
)
class InsertionSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        for (i in 1..<array.size()) {
            val value = array[i]
            var j = i - 1
            script.focus(i, value)
            val swap = script.bulkSwap()
            probe.increment(COMPARISONS, ITERATIONS)
            while (j >= 0 && array[j] > value) {
                probe.increment(ITERATIONS)
                array[j + 1] = array[j]
                swap.add(Pair(j, j + 1))
                j--
            }

            if (swap.isNotEmpty()) {
                array[j + 1] = 0
                script.swap(swap, value)
            }

            if (i != j + 1) {
                array[j + 1] = value
                script.select(j + 1, value)
            }
        }
    }
}