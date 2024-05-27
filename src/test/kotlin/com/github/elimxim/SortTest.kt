package com.github.elimxim

import com.github.elimxim.sort.BubbleSort
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
        actual = IntArrayWrapper(ArrayGenerator.generate(1, 1000), probe, script)
    }

    @Test
    fun testBubbleSort() {
        BubbleSort(probe, script).sort(actual)
        val expected = ArrayGenerator.generate(1, 1000, shuffle = false)
        assertContentEquals(expected, actual.array())
    }

    @Test
    fun testSelectionSort() {
        SelectionSort(probe, script).sort(actual)
        val expected = ArrayGenerator.generate(1, 1000, shuffle = false)
        assertContentEquals(expected, actual.array())
    }
}