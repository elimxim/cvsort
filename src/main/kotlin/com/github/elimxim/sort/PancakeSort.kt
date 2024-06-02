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
        m = n
        while m > 1 do
            max = 0
            for i in (0..m) do
                if array[i] > array[max] then
                    max = i
                end
            end
            
            m--
            
            if max != m then
                if max != 0 then
                    invoke flip(array, max)
                end
                invoke flip(array, m)
            end
        end
        
        fun flip(array, k)
            j = 0
            while j < k do
                swap array[j] and array[k]
                j++ 
                k--
            end
        end
        """
)
class PancakeSort(
        private val probe: Probe,
        private val script: SortScript
): Sort {
    override fun sort(array: IntArrayWrapper) {
        val flip = fun (idx: Int) {
            var start = 0
            var end = idx
            val replace = script.bulkReplace(array.original())
            while (start <= end) {
                probe.increment(ITERATIONS)
                array.swap(start, end)
                replace.replace(focused = start, selected = end)
                replace.replace(focused = end, selected = start)
                start++
                end--
            }

            if (replace.isNotEmpty()) {
                script.replace(replace)
            }
        }

        var lastIdx = array.size()
        while (lastIdx > 1) {
            probe.increment(ITERATIONS)

            var maxIdx = 0
            for (i in 1..<lastIdx) {
                probe.increment(ITERATIONS, COMPARISONS)
                script.focus(i)
                if (array[i] > array[maxIdx]) {
                    maxIdx = i
                }
            }

            lastIdx--

            if (maxIdx != lastIdx) {
                if (maxIdx != 0) {
                    script.select(setOf(maxIdx))
                    flip(maxIdx)
                }
                flip(lastIdx)
            }
        }
    }
}