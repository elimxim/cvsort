package com.github.elimxim.view

import com.github.elimxim.*
import com.github.elimxim.ComplexityClass.*
import de.vandermeer.asciitable.AT_Context
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine

class SortClassificationView : View {
    private val content: MutableList<Pair<SortName, SortClassification>> = ArrayList()

    fun add(sortName: SortName, anno: SortClassification) {
        content.add(Pair(sortName, anno))
    }

    override fun lines(): List<String> {
        return if (content.size >= 1) {
            val ctx = AT_Context()
            val table = AsciiTable(ctx)

            table.addRule()
            table.addRow(listOf(
                    SORT,
                    WORST_TIME,
                    AVERAGE_TIME,
                    BEST_TIME,
                    MEMORY_USAGE,
                    METHODS,
                    RECURSIVE,
                    STABLE
            ))
            table.addRule()
            content.forEach {
                table.addRow(listOf(
                        it.first.name.snakeCaseToCamelCase(separator = " "),
                        complexity(it.second.timeComplexity.worst, BIG_O),
                        complexity(it.second.timeComplexity.average, BIG_THETA),
                        complexity(it.second.timeComplexity.best, BIG_OMEGA),
                        complexity(it.second.spaceComplexity, BIG_O),
                        methods(it.second.methods),
                        it.second.recursive.yesNo(),
                        it.second.stable.yesNo()
                ))
            }
            table.addRule()

            table.setPaddingLeftRight(1, 1)
            table.renderer.setCWC(CWC_LongestLine())

            table.renderAsCollection().toList()
        } else {
            emptyList()
        }
    }

    private fun complexity(complexity: Complexity, complexityClass: ComplexityClass): String {
        return "${complexityClass.notation}(${complexity.notation})"
    }

    private fun methods(methods: Array<Method>): String {
        return methods.joinToString(separator = "<br>& ") { it.camelCase() }
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
    }
}