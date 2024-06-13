package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.LINEAR
        ),
        spaceComplexity = Complexity.CONST,
        methods = [Method.EXCHANGING],
        recursive = false,
        stable = true,
        pseudoCode = """
        idx = 0
        while idx < n do
            if idx = 0 or array[idx-1] < array[idx] then
                idx++
            else 
                swap array[idx-1] and array[idx]
                idx--
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
            script.action(Focus(index))
            probe.increment(ITERATIONS, COMPARISONS)
            if (index == 0 || array[index - 1] < array[index]) {
                index++
            } else {
                array.swap(index - 1, index)
                script.action(Swap(index - 1, index))
                index--
            }
        }
    }
}