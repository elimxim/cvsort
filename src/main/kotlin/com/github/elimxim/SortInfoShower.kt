package com.github.elimxim

import com.github.elimxim.console.Console

class SortInfoShower(private val forceExtraInfo: Boolean) {
    fun showInfo(sortNames: List<SortName>) {
        var actualSortNames = sortNames
        if (sortNames.contains(SortName.ALL)) {
            actualSortNames = sortedEnumValues<SortName>(exclude = arrayOf(SortName.ALL)).toList()
        }

        SortInfoPrinter(withExtraInfo = forceExtraInfo).print(actualSortNames)

        if (actualSortNames.size == 1) {
            val anno = SortFactory.classification(actualSortNames.first())
            if (anno != null) {
                printExtraInfo(anno.extraInfo)
                Console.printEmptyLine()
                printPseudoCode(anno)
            }
        }
    }

    private fun printExtraInfo(anno: ExtraInfo) {
        if (forceExtraInfo.not() && anno.inventionYear != 0) {
            Console.printLine("${YEAR}: ${anno.inventionYear}")
        }
        if (forceExtraInfo.not() && anno.authors.isNotEmpty()) {
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