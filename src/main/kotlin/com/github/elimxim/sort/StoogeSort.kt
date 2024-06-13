package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.TWO_THIRDS_CUBIC,
                average = Complexity.TWO_THIRDS_CUBIC,
                best = Complexity.TWO_THIRDS_CUBIC
        ),
        spaceComplexity = Complexity.LINEAR,
        methods = [],
        recursive = true,
        stable = false,
        pseudoCode = """
        invoke stooge(array, 0, n-1)
        
        fun stooge(array, i, j)
            if array[i] > array[j] then
                swap array[i] and array[j]
            end
            
            if j - i + 1 > 2 then
                t = floor (j - i + 1) / 3
                invoke stooge(array, i, j-t)
                invoke stooge(array, i+t, j)
                invoke stooge(array, i, j-t)
            end
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 0,
                authors = [],
                wikiUrl = "https://en.wikipedia.org/wiki/Stooge_sort"
        )
)
class StoogeSort (
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        stooge(array, i = 0, j = array.size() - 1)
    }

    private fun stooge(array: IntArrayWrapper, i: Int, j: Int) {
        probe.increment(COMPARISONS)
        if (array[i] > array[j]) {
            array.swap(i, j)
            script.action(Swap(i, j))
        }

        if (j - i + 1 > 2) {
            val third = (j - i + 1) / 3
            stooge(array, i, j - third)
            stooge(array, i + third, j)
            stooge(array, i, j - third)
        }
    }
}