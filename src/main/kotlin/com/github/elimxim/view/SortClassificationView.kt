package com.github.elimxim.view

import com.github.elimxim.*
import de.vandermeer.asciitable.AT_Context
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine
import kotlin.reflect.full.findAnnotation

class SortView : View {
    private val content: MutableList<List<String>> = ArrayList()

    fun add(sortName: SortName) {
        val impl = SortFactory.kClass(sortName)
        val anno = impl.findAnnotation<SortClassification>()

        if (anno != null) {
            content.add(listOf(
                    sortName.name.snakeCaseToCamelCase(separator = " "),
                    complexity(anno.timeComplexity.worst, ComplexityClass.BIG_O),
                    complexity(anno.timeComplexity.average, ComplexityClass.BIG_THETA),
                    complexity(anno.timeComplexity.best, ComplexityClass.BIG_OMEGA),
                    complexity(anno.spaceComplexity, ComplexityClass.BIG_O),
                    methods(anno.methods),
                    anno.recursive.yesNo(),
                    anno.stable.yesNo()
            ))
        }
    }

    private fun complexity(complexity: Complexity, complexityClass: ComplexityClass): String {
        return "${complexityClass.notation}(${complexity.notation})"
    }

    private fun methods(methods: Array<Method>): String {
        return methods.joinToString(separator = "<br>& ") { it.camelCase() }
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
            content.forEach { table.addRow(it) }
            table.addRule()

            table.setPaddingLeftRight(1, 1)
            table.renderer.setCWC(CWC_LongestLine())

            table.renderAsCollection().toList()
        } else {
            emptyList()
        }
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