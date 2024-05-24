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

    fun instance(algorithm: Algorithm, probe: Probe): Sort {
        val kClass = implementations[algorithm]
        return kClass!!.primaryConstructor!!.call(probe, SortScriptWriter(probe))
    }

    fun instance(algorithm: Algorithm, probe: Probe, scriptWriter: SortScriptWriter): Sort {
        val kClass = implementations[algorithm]
        return kClass!!.primaryConstructor!!.call(probe, scriptWriter)
    }

    fun impl(algorithm: Algorithm): KClass<out Sort> {
        return implementations[algorithm]!!
    }
}