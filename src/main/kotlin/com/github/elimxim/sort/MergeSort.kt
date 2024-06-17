package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortClassification(
        timeComplexity = TimeComplexity(
                worst = Complexity.LINEARITHMIC,
                average = Complexity.LINEARITHMIC,
                best = Complexity.LINEARITHMIC
        ),
        spaceComplexity = Complexity.LINEARITHMIC,
        methods = [Method.MERGING],
        recursive = true,
        stable = true,
        pseudoCode = """
        invoke merge(array, 0, n-1)
        
        fun merge(array, l, r)
            if l < r then
                m = l + (r - l) / 2

                invoke merge(array, l, m)
                invoke merge(array, m+1, r)
                
                ln = m - l + 1
                rn = r - m
                
                la = [ln]
                ra = [rn]
                
                for i in [0..ln) do
                    la[i] = array[l+i]
                end
                
                for i in [0..rn) do
                    ra[i] = array[m+1+i]
                end
                
                i = 0
                j = 0
                k = l
                while i < ln and j < rn do
                    if la[i] <= ra[j] then
                        array[k++] = la[i++]
                    else
                        array[k++] = ra[j++]
                    end
                end
                
                while i < ln do
                    array[k++] = la[i++]
                end
                
                while j < rn do
                    array[k++] = ra[j++]
                end
            end
        end
        """,
        extraInfo = ExtraInfo(
                inventionYear = 1945,
                authors = ["John von Neumann"],
                wikiUrl = "https://en.wikipedia.org/wiki/Merge_sort"
        )
)
class MergeSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        merge(array, 0, array.size() - 1)
    }

    private fun merge(array: IntArrayWrapper, left: Int, right: Int) {
        probe.increment(ITERATIONS)
        if (left < right) {
            val mid = left + (right - left) / 2

            merge(array, left, mid)
            merge(array, mid + 1, right)

            val leftSize = mid - left + 1
            val rightSize = right - mid

            val leftArray = IntArray(leftSize)
            val rightArray = IntArray(rightSize)

            for (i in 0..<leftSize) {
                probe.increment(ITERATIONS)
                leftArray[i] = array[left + i]
            }

            for (i in 0..<rightSize) {
                probe.increment(ITERATIONS)
                rightArray[i] = array[mid + 1 + i]
            }

            script.scene {
                val extra = leftArray + IntArray(1) + rightArray
                val lf = (left..<left + leftSize).toSet()
                val rf = (mid + 1..<mid + 1 + rightSize).toSet()
                it.action(Focus(lf + rf), Extra(extra))
            }

            var i = 0
            var j = 0
            var k = left
            while (i < leftSize && j < rightSize) {
                probe.increment(ITERATIONS, COMPARISONS)
                if (leftArray[i] <= rightArray[j]) {
                    array[k++] = leftArray[i++]
                    script.scene {
                        val extra = leftArray + IntArray(1) + rightArray
                        val ei = i - 1
                        val ej = leftArray.size + 1 + j
                        it.action(Select(k - 1), Extra(extra, Focus(ej), Select(ei)))
                    }
                } else {
                    array[k++] = rightArray[j++]
                    script.scene {
                        val extra = leftArray + IntArray(1) + rightArray
                        val ei = i
                        val ej = leftArray.size + j
                        it.action(Select(k - 1), Extra(extra, Focus(ei), Select(ej)))
                    }
                }
            }

            while (i < leftSize) {
                probe.increment(ITERATIONS)
                array[k++] = leftArray[i++]
                script.scene {
                    val extra = leftArray + IntArray(1) + rightArray
                    val ei = i
                    val ej = leftArray.size + 1 + j
                    it.action(Select(k - 1), Extra(extra, Focus(ej), Select(ei)))
                }
            }

            while (j < rightSize) {
                probe.increment(ITERATIONS)
                array[k++] = rightArray[j++]
                script.scene {
                    val extra = leftArray + IntArray(1) + rightArray
                    val ei = i
                    val ej = leftArray.size + j
                    it.action(Select(k - 1), Extra(extra, Focus(ei), Select(ej)))
                }
            }
        }
    }
}