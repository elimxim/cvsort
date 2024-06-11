package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.QUADRATIC
        ),
        spaceComplexity = Complexity.CONST,
        methods = [Method.SELECTION],
        stable = false,
        pseudoCode = """
        for i in [0..n-1) do
            v = array[i]
            
            idx = i
            for j in (i..n) do
                if array[j] < v then
                    idx++
                end
            end
            
            if idx = i then
                continue
            end
            
            while v != array[idx] do
                idx++
            end
            
            swap array[idx] and v
            
            while idx != i do
                idx = i
                for j in (i..n) do
                    if array[j] < v then
                        idx++
                    end
                end
                
                while v != array[idx] do
                    idx++
                end
                
                swap array[idx] and v
            end
        end
        """
)
class CycleSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        for (i in 0..<array.size() - 1) {
            probe.increment(ITERATIONS)
            var value = array[i]
            var index = i
            script.action(Focus(index, value))
            for (j in i + 1..<array.size()) {
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[j] < value) {
                    index++
                    script.action(Focus(index), Extra(value))
                }
            }

            if (index == i) {
                continue
            }

            while (value == array[index]) {
                index++
                script.action(Focus(index), Extra(value))
            }

            array[index] = value.also {
                value = array[index]
            }
            script.action(Select(index), Extra(value))

            while (index != i) {
                index = i
                script.action(Focus(index), Extra(value))
                for (j in i + 1..<array.size()) {
                    probe.increment(ITERATIONS, COMPARISONS)
                    if (array[j] < value) {
                        index++
                        script.action(Focus(index), Extra(value))
                    }
                }

                while (value == array[index]) {
                    index++
                    script.action(Focus(index), Extra(value))
                }

                array[index] = value.also {
                    value = array[index]
                }
                script.action(Select(index), Extra(value))
            }
        }
    }
}