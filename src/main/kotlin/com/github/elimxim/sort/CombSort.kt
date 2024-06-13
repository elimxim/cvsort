package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*
import kotlin.math.floor

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.QUADRATIC,
                best = Complexity.LINEARITHMIC
        ),
        spaceComplexity = Complexity.CONST,
        methods = [Method.EXCHANGING],
        recursive = false,
        stable = false,
        pseudoCode = """        
        gap = n
        swapped = true
        
        while swapped do    
            gap = floor gap / 1.3
            if gap <= 1 then
                swapped = false
                gap = 1
            end
            
            for i in [0..n-gap) do
                if array[i] > array[i+gap] then
                    swap array[i] and array[i+gap]
                    swapped = true
                end
            end
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1991,
                authors = ["Stephen Lacey", "Richard Box"],
                wikiUrl = "https://en.wikipedia.org/wiki/Comb_sort"
        )
)
class CombSort (
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        var gap = array.size()
        var swapped = true

        while (swapped) {
            probe.increment(ITERATIONS)
            gap = floor(gap / 1.3).toInt()
            if (gap <= 1) {
                swapped = false
                gap = 1
            }

            for (i in 0..<array.size() - gap) {
                script.action(Focus(i, i + gap))
                probe.increment(ITERATIONS, COMPARISONS)
                if (array[i] > array[i + gap]) {
                    array.swap(i, i + gap)
                    script.action(Swap(i, i + gap))
                    swapped = true
                }
            }
        }
    }
}