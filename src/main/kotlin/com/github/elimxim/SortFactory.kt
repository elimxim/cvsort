package com.github.elimxim

import com.github.elimxim.sort.BubbleSort
import com.github.elimxim.sort.SelectionSort
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object SortFactory {
    private val implementations = mapOf(
            Pair(Algorithm.BUBBLE, BubbleSort::class),
            Pair(Algorithm.SELECTION, SelectionSort::class)
    )

    fun instance(algorithm: Algorithm, counter: Counter): Sort {
        val kClass = implementations[algorithm]
        return kClass!!.primaryConstructor!!.call(counter)
    }

    fun impl(algorithm: Algorithm): KClass<out Sort> {
        return implementations[algorithm]!!
    }
}