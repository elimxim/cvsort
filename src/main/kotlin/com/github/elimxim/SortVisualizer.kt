package com.github.elimxim

import com.github.elimxim.console.SortView
import com.github.elimxim.console.ArrayView
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.ProbeView
import kotlinx.coroutines.*

class SortVisualizer(
        private val speedMillis: Long,
        private val arrayLength: Int,
        private val showInfo: Boolean
) {
    fun visualize(sortName: SortName) {
        if (showInfo) {
            val sortView = SortView()
            sortView.add(sortName)
            sortView.print()
        }

        runBlocking {
            withContext(Dispatchers.Default) {
                runVisualize(sortName)
            }
        }
    }

    private suspend fun runVisualize(sortName: SortName) {
        val probe = Probe()
        val array = (1..arrayLength).toIntArray()
        val arrayWrapper = IntArrayWrapper(array, probe)
        val opening = SortScriptImpl(probe, arrayWrapper)

        printOpening(sortName, opening, array)
        delay(AFTER_OPENING_DELAY)

        val script = SortScriptImpl(probe, arrayWrapper)
        val sort = SortFactory.instance(sortName, probe, script)
        sort.sort(arrayWrapper)

        val screenplay = script.screenplay()
        while (screenplay.isNotEmpty()) {
            delay(speedMillis)
            printScriptLine(sortName, screenplay.poll(), refresh = true)
        }

        val ending = SortScriptImpl(probe, arrayWrapper)
        printEnding(sortName, ending, array)
    }

    private suspend fun printOpening(sortName: SortName, opening: SortScript, array: IntArray) {
        ArrayShuffler(opening).shuffle(array)
        val screenplay = opening.screenplay()
        printScriptLine(sortName, screenplay.poll())
        while (screenplay.isNotEmpty()) {
            delay(OPENING_DELAY)
            printScriptLine(sortName, screenplay.poll(), refresh = true)
        }
    }

    private suspend fun printEnding(sortName: SortName, ending: SortScript, array: IntArray) {
        for (i in array.indices) {
            ending.line(Focus(i))
        }
        val indexes = mutableSetOf<Int>()
        for (i in array.indices) {
            indexes.add(i)
            ending.line(Focus(*indexes.toIntArray()))
        }
        val screenplay = ending.screenplay()
        while (screenplay.isNotEmpty()) {
            delay(ENDING_DELAY)
            printScriptLine(sortName, screenplay.poll(), refresh = true)
        }
    }

    private fun printScriptLine(sortName: SortName, scriptLine: ScriptLine, refresh: Boolean = false) {
        val proveView = ProbeView(sortName, scriptLine.probeSnapshot)
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