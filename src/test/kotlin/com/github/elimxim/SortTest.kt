package com.github.elimxim

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.*

class SortTest {

    @TestFactory
    fun dynamicTests(): List<DynamicTest> {
        return Algorithm.entries.map { dynamicTest(it) }.toList()
    }

    private fun dynamicTest(algorithm: Algorithm) = DynamicTest.dynamicTest("${algorithm.canonicalName()} test") {
        val probe = Probe(algorithm)
        val sort = SortFactory.instance(algorithm, probe)

        val expectedArray = (1..1000).toList().toIntArray()
        val actualArray = (1..1000).shuffled().toIntArray()

        assertFalse { expectedArray.contentEquals(actualArray) }

        val wrapper = IntArrayWrapper(actualArray, probe)
        sort.sort(wrapper)

        assertContentEquals(expectedArray, wrapper.original())
    }
}