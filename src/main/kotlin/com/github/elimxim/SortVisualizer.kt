package com.github.elimxim

import com.github.elimxim.console.AlgorithmView
import com.github.elimxim.console.ArrayView
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.ProbeView
import kotlinx.coroutines.*
import kotlin.reflect.full.findAnnotation

class SortVisualizer(
        private val speed: SortSpeed,
        private val showVisualization: Boolean,
        private val showPseudoCode: Boolean,
        private val showInfo: Boolean
) {
    fun visualize(algorithm: Algorithm) {
        if (showInfo) {
            val table = AlgorithmView()
            table.add(algorithm)
            table.print()
        }

        if (showPseudoCode) {
            printPseudoCode(algorithm)
        }

        if (showVisualization) {
            runBlocking {
                withContext(Dispatchers.Default) {
                    visualiseAlgorithm(algorithm)
                }
            }
        }
    }

    private fun printPseudoCode(algorithm: Algorithm) {
        val impl = SortFactory.kClass(algorithm)
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

    private suspend fun visualiseAlgorithm(algorithm: Algorithm) {
        val array = ArrayGenerator.generate(1, 16)
        val probe = Probe(algorithm)
        val script = SortScriptImpl(probe)
        val arrayWrapper = IntArrayWrapper(array, probe, script)

        val sort = SortFactory.instance(algorithm, probe, script)
        sort.sort(arrayWrapper)

        script.focusEvery(arrayWrapper)
        script.focusAll(arrayWrapper);

        val scriptLines = script.scriptLines()
        printScriptLine(scriptLines.poll())
        while (!scriptLines.isEmpty()) {
            delay(speed.millis)
            printScriptLine(scriptLines.poll(), refresh = true)
        }
    }

    private fun printScriptLine(scriptLine: ScriptLine, refresh: Boolean = false) {
        val lines = mutableListOf<String>()
        lines.addAll(ArrayView(scriptLine.array, scriptLine.focus).lines())
        lines.add("")
        lines.addAll(ProbeView(scriptLine.probeSnapshot).lines())
        ConsolePrinter.printLines(lines, refresh)
    }
}