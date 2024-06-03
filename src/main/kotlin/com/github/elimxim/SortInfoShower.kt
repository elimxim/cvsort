package com.github.elimxim

import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.SortView
import kotlin.reflect.full.findAnnotation

class SortInfoShower {
    fun showInfo(sortNames: List<SortName>) {
        val view = SortView()
        sortNames.distinct().forEach(view::add)
        view.print()

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
                ConsolePrinter.printLine(text)
                ConsolePrinter.printEmptyLine()
            } else {
                ConsolePrinter.printError("pseudo code is not implemented")
            }
        } else {
            ConsolePrinter.printError("pseudo code is not implemented")
        }
    }
}