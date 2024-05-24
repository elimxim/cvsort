package com.github.elimxim

import com.github.elimxim.console.ConsolePrinter
import de.vandermeer.asciitable.AsciiTable
import kotlin.reflect.full.findAnnotation

class AlgorithmComplexity {
    private val content: MutableList<List<String>> = ArrayList()

    fun add(algorithm: Algorithm) {
        val impl = SortFactory.impl(algorithm)
        val anno = impl.findAnnotation<SortAlgorithm>()

        if (anno != null) {
            content.add(listOf(
                    algorithm.canonicalName(),
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

    fun print() {
        if (content.size >= 1) {
            val table = AsciiTable()

            table.addRule()
            table.addRow(mutableListOf(
                    ALGORITHM,
                    WORST_TIME,
                    AVERAGE_TIME,
                    BEST_TIME,
                    SPACE
            ))
            table.addRule()
            content.forEach { table.addRow(it) }
            table.addRule()

            ConsolePrinter.printLines(table.renderAsCollection().toList())
            ConsolePrinter.printEmptyLine()
        } else {
            ConsolePrinter.printError("algorithm complexity is not written")
        }
    }

    private companion object Header {
        const val ALGORITHM = "Algorithm"
        const val WORST_TIME = "Worst time"
        const val AVERAGE_TIME = "Average time"
        const val BEST_TIME = "Best time"
        const val SPACE = "Space"
    }
}