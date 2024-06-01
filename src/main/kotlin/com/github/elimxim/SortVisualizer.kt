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
                    runVisualize(sortName)
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

    private suspend fun runVisualize(sortName: SortName) {
        val probe = Probe(sortName)

        val arrayWrapper = printOpening(probe)
        delay(AFTER_OPENING_DELAY)

        val script = SortScriptImpl(probe)
        val sort = SortFactory.instance(sortName, probe, script)
        sort.sort(arrayWrapper)

        val scriptLines = script.scriptLines()
        while (scriptLines.isNotEmpty()) {
            delay(speedMillis)
            printScriptLine(scriptLines.poll(), refresh = true)
        }

        printEnding(probe, arrayWrapper)
    }

    private suspend fun printOpening(probe: Probe): IntArrayWrapper {
        val opening = SortScriptImpl(probe)
        val array = ArrayGenerator(opening).generate(1, arrayLength)
        val openingLines = opening.scriptLines()
        printScriptLine(openingLines.poll())
        while (openingLines.isNotEmpty()) {
            delay(OPENING_DELAY)
            printScriptLine(openingLines.poll(), refresh = true)
        }

        return IntArrayWrapper(array, probe)
    }

    private suspend fun printEnding(probe: Probe, arrayWrapper: IntArrayWrapper) {
        val ending = SortScriptImpl(probe)
        val array = arrayWrapper.original()
        for (i in 0..<arrayWrapper.size()) {
            ending.focus(array, i)
        }
        val indexes = mutableSetOf<Int>()
        for (i in array.indices) {
            indexes.add(i)
            ending.focus(array, *indexes.toIntArray())
        }
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
                variable = scriptLine.variable,
                focused = scriptLine.focused,
                selected = scriptLine.selected
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