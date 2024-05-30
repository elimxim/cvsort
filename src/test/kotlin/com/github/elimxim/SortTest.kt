package com.github.elimxim

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class SortTest {

//    @Disabled
    @TestFactory
    fun dynamicTests(): List<DynamicTest> {
        return Algorithm.entries.map { dynamicTest(it) }.toList()
    }

    private fun dynamicTest(algorithm: Algorithm) = DynamicTest.dynamicTest("${algorithm.canonicalName()} test") {
        val probe = Probe(algorithm)
        val sort = SortFactory.instance(algorithm, probe)

        val expectedArray = (1..1000).toList().toIntArray()
        val actualArray = (1..1000).shuffled().toIntArray()

        assertMonotonicallyIncreasing(expectedArray)

        val wrapper = IntArrayWrapper(actualArray, probe)
        sort.sort(wrapper)

        assertArrayEquals(expectedArray, wrapper.original())
    }
}