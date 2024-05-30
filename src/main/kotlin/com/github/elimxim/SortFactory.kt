package com.github.elimxim

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
            SortName.BUBBLE -> BubbleSort::class
            SortName.SELECTION -> SelectionSort::class
            SortName.INSERTION -> InsertionSort::class
            SortName.GNOME -> GnomeSort::class
            SortName.COCKTAIL_SHAKER -> CocktailShakerSort::class
            SortName.ODD_EVEN -> OddEvenSort::class
        }
    }
}