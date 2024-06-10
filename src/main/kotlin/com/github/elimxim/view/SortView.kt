package com.github.elimxim.view

import com.github.elimxim.*
import com.github.elimxim.console.Console
import de.vandermeer.asciitable.AsciiTable
import kotlin.reflect.full.findAnnotation

class SortView : View {
    private val content: MutableList<List<String>> = ArrayList()

    fun add(sortName: SortName) {
        val impl = SortFactory.kClass(sortName)
        val anno = impl.findAnnotation<SortAlgorithm>()

        if (anno != null) {
            content.add(listOf(
                    sortName.canonical(),
                    complexity(anno.timeComplexity.worst, ComplexityClass.BIG_O),
                    complexity(anno.timeComplexity.average, ComplexityClass.BIG_THETA),
                    complexity(anno.timeComplexity.best, ComplexityClass.BIG_OMEGA),
                    complexity(anno.spaceComplexity, ComplexityClass.BIG_O)
            ))
        }
    }

    private fun complexity(complexity: Complexity, complexityClass: ComplexityClass): String {
        return "${complexityClass.notation}(${complexity.notation})"
    }

    override fun lines(): List<String> {
        return if (content.size >= 1) {
            val table = AsciiTable()

            table.addRule()
            table.addRow(listOf(
                    ALGORITHM,
                    WORST_TIME,
                    AVERAGE_TIME,
                    BEST_TIME,
                    MEMORY_USAGE
            ))
            table.addRule()
            content.forEach { table.addRow(it) }
            table.addRule()

            table.renderAsCollection().toList()
        } else {
            emptyList()
        }
    }

    private companion object Header {
        const val ALGORITHM = "Algorithm"
        const val WORST_TIME = "Worst time"
        const val AVERAGE_TIME = "Average time"
        const val BEST_TIME = "Best time"
        const val MEMORY_USAGE = "Memory usage"
    }
}