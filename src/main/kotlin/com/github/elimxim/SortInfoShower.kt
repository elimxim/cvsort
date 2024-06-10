package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.view.SortView
import kotlin.reflect.full.findAnnotation

class SortInfoShower {
    fun showInfo(sortNames: List<SortName>) {
        val view = SortView()
        sortNames.distinct().forEach(view::add)
        Console.printLines(view.lines())
        Console.printEmptyLine()

        if (sortNames.size == 1) {
            printPseudoCode(sortNames.first())
        }
    }

    private fun printPseudoCode(sortName: SortName) {
        val impl = SortFactory.kClass(sortName)
        val anno = impl.findAnnotation<SortAlgorithm>()

        if (anno != null) {
            val text = anno.pseudoCode.trimIndent()
            if (text.isNotEmpty()) {
                Console.printLine(text)
                Console.printEmptyLine()
            } else {
                Console.printError("pseudo code is not implemented")
            }
        } else {
            Console.printError("pseudo code is not implemented")
        }
    }
}