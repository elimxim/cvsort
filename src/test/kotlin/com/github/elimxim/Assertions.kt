package com.github.elimxim

import kotlin.test.assertEquals

fun assertMonotonicallyIncreasing(array: IntArray, incrementBy: Int = 1) {
    var prev = array.first()
    array.drop(1).forEachIndexed { index, i ->
        val curr = prev + incrementBy
        assertEquals(curr, i, "expected ${prev + incrementBy} at index $index but found $i")
        prev = curr
    }
}

fun assertArrayEquals(expected: IntArray, actual: IntArray) {
    expected.forEachIndexed { index, i ->
        assertEquals(i, actual[index], "expected $i at index $index but found ${actual[index]}")
    }
}