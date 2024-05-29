package com.github.elimxim

import com.github.elimxim.sort.BubbleSort
import com.github.elimxim.sort.InsertionSort
import com.github.elimxim.sort.SelectionSort
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals

class SortTest {
    private lateinit var probe: Probe
    private var script: SortScript = NoOpSortScript()
    private lateinit var actual: IntArrayWrapper

    @BeforeTest
    fun beforeEach() {
        probe = Probe(Algorithm.valueOf(Algorithm.names().random().uppercase()))
        actual = IntArrayWrapper(ArrayGenerator().generate(1, 1000), probe)
    }

    @Test
    fun testBubbleSort() {
        BubbleSort(probe, script).sort(actual)
        val expected = ArrayGenerator().generate(1, 1000, shuffle = false)
        assertContentEquals(expected, actual.original())
    }

    @Test
    fun testSelectionSort() {
        SelectionSort(probe, script).sort(actual)
        val expected = ArrayGenerator().generate(1, 1000, shuffle = false)
        assertContentEquals(expected, actual.original())
    }

    @Test
    fun testInsertionSort() {
        InsertionSort(probe, script).sort(actual)
        val expected = ArrayGenerator().generate(1, 1000, shuffle = false)
        assertContentEquals(expected, actual.original())
    }
}