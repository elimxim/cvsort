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
        idx = 0
        while idx < n do
            if idx = 0 or array[idx-1] < array[idx] then
                idx = idx+1
            else 
                swap array[idx-1] and array[idx]
                idx = idx-1
            end
        end
        """
)
class GnomeSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        var index = 0
        while (index < array.size()) {
            script.focus(array.original(), index)
            probe.increment(ITERATIONS, COMPARISONS)
            if (index == 0 || array[index - 1] < array[index]) {
                index += 1
            } else {
                probe.increment(ARRAY_SWAPS)
                array.swap(index - 1, index)
                script.swap(array.original(), index - 1, index)
                index -= 1
            }
        }
    }
}