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
            j = i-1
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
            script.focus(array.original(), i)
            val value = array[i]
            var j = i - 1
            val selection = script.bulkSelection(array.original())
            probe.increment(COMPARISONS, ITERATIONS)
            while (j >= 0 && array[j] > value) {
                probe.increment(ITERATIONS)
                array[j + 1] = array[j]
                selection.add(Selection(array.original(), j, j + 1))
                j -= 1
            }

            if (selection.isNotEmpty()) {
                array[j + 1] = 0
                script.select(array.original(), selection)
            }

            if (i != j + 1) {
                array[j + 1] = value
                script.select(array.original(), j + 1)
            }
        }
    }
}