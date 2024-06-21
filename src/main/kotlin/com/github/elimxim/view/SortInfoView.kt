package com.github.elimxim.view

import com.github.elimxim.*
import de.vandermeer.asciitable.AT_Context
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment

class SortInfoView(private val withExtraInfo: Boolean = false) : View {
    private val content: MutableList<Pair<SortName, SortClassification>> = ArrayList()

    fun add(sortName: SortName, anno: SortClassification) {
        content.add(Pair(sortName, anno))
    }

    override fun lines(): List<String> {
        return if (content.size >= 1) {
            val ctx = AT_Context()
            val table = AsciiTable(ctx)

            table.addRule()
            table.addRow(header())
            table.addRule()
            content.forEach { table.addRow(row(it)) }
            table.addRule()
            table.setTextAlignment(TextAlignment.LEFT)
            table.setPaddingLeftRight(1, 1)
            table.renderer.setCWC(CWC_LongestLine())

            table.renderAsCollection().toList()
        } else {
            emptyList()
        }
    }

    private fun header(): List<String> {
        val header = mutableListOf(
                SORT,
                WORST_TIME,
                AVERAGE_TIME,
                BEST_TIME,
                MEMORY_USAGE,
                METHODS,
                RECURSIVE,
                STABLE
        )

        if (withExtraInfo) {
            header.add(INVENTION_YEAR)
            header.add(AUTHORS)
        }

        return header.toList()
    }

    private fun row(pair: Pair<SortName, SortClassification>): List<String> {
        val sortName = pair.first
        val anno = pair.second
        val row = mutableListOf(
                sortName.name.snakeCaseToCamelCase(separator = " "),
                complexity(anno.timeComplexity.worst, ComplexityClass.BIG_O),
                complexity(anno.timeComplexity.average, ComplexityClass.BIG_THETA),
                complexity(anno.timeComplexity.best, ComplexityClass.BIG_OMEGA),
                complexity(anno.spaceComplexity, ComplexityClass.BIG_O),
                methods(anno.methods),
                anno.recursive.yesNo(),
                anno.stable.yesNo()
        )

        if (withExtraInfo) {
            if (anno.extraInfo.inventionYear != 0) {
                row.add(anno.extraInfo.inventionYear.toString())
            } else {
                row.add("")
            }
            row.add(authors(anno.extraInfo.authors))
        }

        return row.toList()
    }

    private fun complexity(complexity: Complexity, complexityClass: ComplexityClass): String {
        return "${complexityClass.notation}(${complexity.notation})"
    }

    private fun methods(methods: Array<SortMethod>): String {
        return methods.joinToString(separator = "<br>& ") { it.camelCase() }
    }

    private fun authors(authors: Array<String>): String {
        return authors.joinToString(separator = "<br>& ")
    }

    private companion object Header {
        const val SORT = "Sort"
        const val WORST_TIME = "Worst time"
        const val AVERAGE_TIME = "Average time"
        const val BEST_TIME = "Best time"
        const val MEMORY_USAGE = "Memory"
        const val METHODS = "Methods"
        const val RECURSIVE = "Recursive"
        const val STABLE = "Stable"
        const val INVENTION_YEAR = "Year"
        const val AUTHORS = "Authors"
    }
}