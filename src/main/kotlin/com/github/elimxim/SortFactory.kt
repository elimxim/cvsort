package com.github.elimxim

import com.github.elimxim.sort.BubbleSort
import com.github.elimxim.sort.GnomeSort
import com.github.elimxim.sort.InsertionSort
import com.github.elimxim.sort.SelectionSort
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object SortFactory {
    private val implementations = mapOf(
            Pair(Algorithm.BUBBLE, BubbleSort::class),
            Pair(Algorithm.SELECTION, SelectionSort::class),
            Pair(Algorithm.INSERTION, InsertionSort::class),
            Pair(Algorithm.GNOME, GnomeSort::class)
    )

    fun instance(algorithm: Algorithm, probe: Probe): Sort {
        val kClass = implementations[algorithm]
        return kClass!!.primaryConstructor!!.call(probe, NoOpSortScript())
    }

    fun instance(algorithm: Algorithm, probe: Probe, scriptWriter: SortScript): Sort {
        val kClass = implementations[algorithm]
        return kClass!!.primaryConstructor!!.call(probe, scriptWriter)
    }

    fun kClass(algorithm: Algorithm): KClass<out Sort> {
        return implementations[algorithm]!!
    }
}