package com.github.elimxim

import com.github.elimxim.console.ConsolePrinter
import kotlin.reflect.full.findAnnotation

class SortVisualizer(
        private val showVisualization: Boolean,
        private val showPseudoCode: Boolean,
        private val showInfo: Boolean
) {
    fun visualize(algorithm: Algorithm) {
        if (showInfo) {
            val table = AlgorithmComplexity()
            table.add(algorithm)
            table.print()
        }

        if (showPseudoCode) {
            printPseudoCode(algorithm)
        }

        if (showVisualization) {

        }
    }

    private fun printPseudoCode(algorithm: Algorithm) {
        val impl = SortFactory.impl(algorithm)
        val anno = impl.findAnnotation<SortAlgorithm>()

        if (anno != null) {
            val text = anno.pseudoCode.trimIndent()
            if (text.isNotEmpty()) {
                ConsolePrinter.printLine(text)
            } else {
                ConsolePrinter.printError("pseudo code is not implemented")
            }
        } else {
            ConsolePrinter.printError("pseudo code is not implemented")
        }
    }
}