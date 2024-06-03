package com.github.elimxim

import com.github.elimxim.SortName.*
import com.github.elimxim.sort.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object SortFactory {
    fun instance(sortName: SortName, probe: Probe): Sort {
        val kClass = getClass(sortName)
        return kClass.primaryConstructor!!.call(probe, NoOpSortScript())
    }

    fun instance(sortName: SortName, probe: Probe, scriptWriter: SortScript): Sort {
        val kClass = getClass(sortName)
        return kClass.primaryConstructor!!.call(probe, scriptWriter)
    }

    fun kClass(sortName: SortName): KClass<out Sort> {
        return getClass(sortName)
    }

    private fun getClass(sortName: SortName): KClass<out Sort> {
        return when (sortName) {
            BUBBLE -> BubbleSort::class
            SELECTION -> SelectionSort::class
            INSERTION -> InsertionSort::class
            GNOME -> GnomeSort::class
            COCKTAIL_SHAKER -> CocktailShakerSort::class
            ODD_EVEN -> OddEvenSort::class
            PANCAKE -> PancakeSort::class
            CYCLE -> CycleSort::class
            STOOGE -> StoogeSort::class
            COMB -> CombSort::class
        }
    }
}