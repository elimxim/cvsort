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
        methods = [Method.SELECTION],
        recursive = false,
        stable = false,
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
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1979,
                authors = ["William H. Gates", "Christos H. Papadimitriou"],
                wikiUrl = "https://en.wikipedia.org/wiki/Pancake_sorting"
        )
)
class PancakeSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        var lastIdx = array.size()
        while (lastIdx > 1) {
            probe.increment(ITERATIONS)
            var maxIdx = 0
            for (i in 1..<lastIdx) {
                script.action(Focus(i))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[i] > array[maxIdx]) {
                    maxIdx = i
                }
            }

            lastIdx--

            if (maxIdx != lastIdx) {
                if (maxIdx != 0) {
                    script.action(Select(maxIdx))
                    flip(array, maxIdx)
                }
                flip(array, lastIdx)
            }
        }
    }

    private fun flip(array: IntArrayWrapper, idx: Int) {
        var start = 0
        var end = idx
        val bulkMove = BulkMove(array)
        while (start <= end) {
            probe.increment(ITERATIONS)
            array.swap(start, end)
            bulkMove.add(start, end)
            bulkMove.add(end, start)
            start++
            end--
        }

        script.action(bulkMove)
    }
}