package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.LINEARITHMIC,
                average = Complexity.LINEARITHMIC,
                best = Complexity.LINEARITHMIC
        ),
        spaceComplexity = Complexity.LOGARITHMIC,
        methods = [Method.SELECTION],
        recursive = true,
        stable = false,
        pseudoCode = """
        m = n / 2 - 1
        for i in [m..0] do
            invoke heapify(array, i, n)
        end
        
        for i in (n..0) do
            swap array[0] array[i]
            invoke heapify(array, 0, i) 
        end
        
        fun heapify(array, i, size)
            root = i
            l = i * 2 + 1
            r = i * 2 + 2
            
            if l < s and array[l] > array[root] then
                root = l
            end
            
            if r < s and array[r] > array[root] then
                root = r 
            end
            
            if root != i then
                swap array[i] and array[root]
                invoke heapify(array, root, size)
            end
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1964,
                authors = ["J. W. J. Williams"],
                wikiUrl = "https://en.wikipedia.org/wiki/Heapsort"
        )
)
class HeapSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val m = array.size() / 2 - 1
        for (i in m downTo 0) {
            probe.increment(ITERATIONS)
            script.action(Focus(i))
            heapify(array, i, array.size())
        }

        for (i in array.size() - 1 downTo 1) {
            probe.increment(ITERATIONS)
            array.swap(0, i)
            script.action(Swap(0, i))
            heapify(array, 0, i)
        }
    }

    private fun heapify(array: IntArrayWrapper, idx: Int, size: Int) {
        var root = idx
        val left = idx * 2 + 1
        val right = idx * 2 + 2

        probe.increment(COMPARISONS)
        if (left < size && array[left] > array[root]) {
            root = left
        }

        probe.increment(COMPARISONS)
        if (right < size && array[right] > array[root]) {
            root = right
        }

        if (root != idx) {
            probe.increment(ITERATIONS)
            script.action(Focus(idx, left, right))
            array.swap(idx, root)
            script.scene {
                val focus = if (root == left) Focus(right) else Focus(left)
                it.action(Select(idx, root), focus)
            }
            heapify(array, root, size)
        }
    }
}