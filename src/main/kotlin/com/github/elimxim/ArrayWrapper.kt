package com.github.elimxim

interface ArrayWrapper<T> {
    operator fun get(index: T): Int
    operator fun set(index: T, value: T)
    fun swap(index1: Int, index2: Int): Boolean
    fun copy(): Array<T>
    fun size(): Int
}