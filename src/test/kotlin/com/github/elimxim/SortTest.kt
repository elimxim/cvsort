package com.github.elimxim

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class SortTest {

    @TestFactory
    fun dynamicTests(): List<DynamicTest> {
        return SortName.entries.map { dynamicTest(it) }.toList()
    }

    private fun dynamicTest(sortName: SortName) = DynamicTest.dynamicTest("${sortName.canonical()} test") {
        val probe = Probe(sortName)
        val sort = SortFactory.instance(sortName, probe)

        val expectedArray = (1..1000).toList().toIntArray()
        val actualArray = (1..1000).shuffled().toIntArray()

        assertMonotonicallyIncreasing(expectedArray)

        val wrapper = IntArrayWrapper(actualArray, probe)
        sort.sort(wrapper)

        assertArrayEquals(expectedArray, wrapper.original())
    }
}