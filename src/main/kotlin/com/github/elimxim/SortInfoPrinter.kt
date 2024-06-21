package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.view.SortInfoView
import java.lang.RuntimeException

class SortInfoPrinter(
        private val ignoreMissedAnno: Boolean = true,
        private val withExtraInfo: Boolean = false
) {
    fun print(sortNames: List<SortName>) {
        val sortView = SortInfoView(withExtraInfo)
        sortNames.distinct().forEach {
            val anno = SortFactory.classification(it)
            if (anno != null) {
                sortView.add(it, anno)
            } else if (ignoreMissedAnno.not()) {
                throw RuntimeException("cannot find information about ${it.canonical()}")
            }
        }
        Console.printLines(sortView.lines())
        Console.printEmptyLine()
    }
}