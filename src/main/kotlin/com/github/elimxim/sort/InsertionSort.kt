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
            probe.increment(ITERATIONS)
            val value = array[i]
            var j = i - 1
            script.line(Focus(i), Extra(value))
            val bulkMove = script.bulkMove()
            while (j >= 0 && array[j] > value) {
                probe.increment(ITERATIONS, COMPARISONS)
                array[j + 1] = array[j]
                bulkMove.add(j, j + 1)
                j--
            }

            if (bulkMove.isNotEmpty()) {
                array[j + 1] = 0
                script.line(bulkMove, Extra(value))
            }

            if (i != j + 1) {
                array[j + 1] = value
                script.line(Select(j + 1), Extra(value))
            }
        }
    }
}