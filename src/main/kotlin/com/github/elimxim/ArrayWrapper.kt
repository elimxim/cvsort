package com.github.elimxim

interface ArrayWrapper<T> {
    operator fun get(index: T): Int
    operator fun set(index: T, value: T)
    fun swap(index1: Int, index2: Int): Boolean
    fun array(): Array<T>
    fun size(): Int
}