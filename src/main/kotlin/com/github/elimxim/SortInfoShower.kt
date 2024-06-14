package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.view.SortClassificationView

class SortInfoShower {
    fun showInfo(sortNames: List<SortName>) {
        val sortMap = sortNames.distinct().mapNotNull {
            val anno = SortFactory.classification(it)
            if (anno != null) Pair(it, anno) else null
        }.toMap()

        val view = SortClassificationView()
        sortMap.forEach { (k, v) -> view.add(k, v) }
        Console.printLines(view.lines())
        Console.printEmptyLine()

        if (sortMap.size == 1) {
            printExtraInfo(sortMap.values.first().extraInfo)
            Console.printEmptyLine()
            printPseudoCode(sortMap.values.first())
        }
    }

    private fun printExtraInfo(anno: ExtraInfo) {
        if (anno.inventionYear != 0) {
            Console.printLine("${YEAR}: ${anno.inventionYear}")
        }
        if (anno.authors.isNotEmpty()) {
            Console.printLine("${AUTHORS}: ${anno.authors.joinToString()}")
        }
        if (anno.wikiUrl.isNotEmpty()) {
            Console.printLine("${WIKI_URL}: ${anno.wikiUrl}")
        }
    }

    private fun printPseudoCode(anno: SortClassification) {
        val text = anno.pseudoCode.trimIndent()
        if (text.isNotEmpty()) {
            Console.printLine(text)
            Console.printEmptyLine()
        } else {
            Console.printError("pseudo code is not implemented")
        }
    }

    private companion object Header {
        const val YEAR = "Year of invention"
        const val AUTHORS = "Author(s)"
        const val WIKI_URL = "Wiki"
    }
}