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
        methods = [Method.INSERTION],
        stable = true,
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
            script.action(Focus(i), Extra(value))
            val bulkMove = BulkMove(array)
            while (j >= 0 && array[j] > value) {
                probe.increment(ITERATIONS, COMPARISONS)
                array[j + 1] = array[j]
                bulkMove.add(j, j + 1)
                j--
            }

            script.scene {
                if (bulkMove.isNotEmpty()) {
                    array[j + 1] = 0
                    script.action(bulkMove, Extra(value))
                }
            }

            if (i != j + 1) {
                array[j + 1] = value
                script.action(Select(j + 1), Extra(value))
            }
        }
    }
}