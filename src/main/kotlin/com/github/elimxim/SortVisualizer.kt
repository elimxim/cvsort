package com.github.elimxim

import com.github.elimxim.console.SortView
import com.github.elimxim.console.ArrayView
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.ProbeView
import kotlinx.coroutines.*
import kotlin.reflect.full.findAnnotation

class SortVisualizer(
        private val speedMillis: Long,
        private val arrayLength: Int,
        private val showVisualization: Boolean,
        private val showPseudoCode: Boolean,
        private val showInfo: Boolean
) {
    fun visualize(sortName: SortName) {
        if (showInfo) {
            val sortView = SortView()
            sortView.add(sortName)
            sortView.print()
        }

        if (showPseudoCode) {
            printPseudoCode(sortName)
        }

        if (showVisualization) {
            runBlocking {
                withContext(Dispatchers.Default) {
                    visualiseSortingAlgorithm(sortName)
                }
            }
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

    private suspend fun visualiseSortingAlgorithm(name: SortName) {
        val probe = Probe(name)
        val opening = SortScriptImpl(probe)

        val array = ArrayGenerator(opening).generate(1, arrayLength)
        val openingLines = opening.scriptLines()
        printScriptLine(openingLines.poll())
        while (openingLines.isNotEmpty()) {
            delay(OPENING_DELAY)
            printScriptLine(openingLines.poll(), refresh = true)
        }

        delay(AFTER_OPENING_DELAY)

        val script = SortScriptImpl(probe)

        val sort = SortFactory.instance(name, probe, script)
        val arrayWrapper = IntArrayWrapper(array, probe)
        sort.sort(arrayWrapper)

        val scriptLines = script.scriptLines()
        while (scriptLines.isNotEmpty()) {
            delay(speedMillis)
            printScriptLine(scriptLines.poll(), refresh = true)
        }

        val ending = SortScriptImpl(probe)
        ending.focusLoop(arrayWrapper.original())
        ending.focusLoopAccumulative(arrayWrapper.original())
        val endingLines = ending.scriptLines()
        while (endingLines.isNotEmpty()) {
            delay(ENDING_DELAY)
            printScriptLine(endingLines.poll(), refresh = true)
        }
    }

    private fun printScriptLine(scriptLine: ScriptLine, refresh: Boolean = false) {
        val proveView = ProbeView(scriptLine.probeSnapshot)
        val arrayView = ArrayView(
                array = scriptLine.array,
                focus = scriptLine.focus,
                select = scriptLine.select
        )

        val lines = mutableListOf<String>()
        lines.addAll(arrayView.lines())
        lines.add("")
        lines.addAll(proveView.lines())
        ConsolePrinter.printLines(lines, refresh)
    }

    private companion object {
        const val OPENING_DELAY: Long = 400
        const val AFTER_OPENING_DELAY: Long = 1000
        const val ENDING_DELAY: Long = 50
    }
}