package com.github.elimxim

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class SortTest {
    @TestFactory
    fun sortTests() = SortName.realValues().map { sortTest(it) }.toList()

    private fun sortTest(sortName: SortName) = DynamicTest.dynamicTest("${sortName.canonical()} test") {
        val probe = Probe()
        val sort = SortFactory.instance(sortName, probe)

        val expectedArray = (1..1000).toList().toIntArray()
        val actualArray = (1..1000).shuffled().toIntArray()

        assertMonotonicallyIncreasing(expectedArray)

        val wrapper = IntArrayWrapper(actualArray, probe)
        sort.sort(wrapper)

        assertArrayEquals(expectedArray, wrapper.snapshot())
    }
}