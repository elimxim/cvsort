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
        i = 0
        m = n - 1
        while i < m do             
            mj = i
            for j in [i..m) do
                if array[j] > array[j+1] then
                    swap array[j] and array[j+1]
                    mj = j
                end
            end
            
            m = mj
            
            ij = m
            for j in [m..i) do
                if array[j] < array[j-1] then
                    swap array[j-1] and array[j]
                    ij = j                                       
                end
            end
            
            i = ij
        end
        """
)
class ShakerSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        var beginIndex = 0
        var endIndex = array.size() - 1

        while (beginIndex < endIndex) {
            probe.increment(ITERATIONS)

            var newEndIndex = beginIndex
            for (i in beginIndex..<endIndex) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.action(Focus(i))
                if (array[i] > array[i + 1]) {
                    array.swap(i, i + 1)
                    script.action(Swap(i, i + 1))
                    newEndIndex = i
                }
            }

            endIndex = newEndIndex

            var newBeginIndex = endIndex
            for (i in endIndex downTo  beginIndex + 1) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.action(Focus(i))
                if (array[i - 1] > array[i]) {
                    script.action(Swap(i - 1, i))
                    array.swap(i - 1, i)
                    newBeginIndex = i
                }
            }

            beginIndex = newBeginIndex
        }
    }
}