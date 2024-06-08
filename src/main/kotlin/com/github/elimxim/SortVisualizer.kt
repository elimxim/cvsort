package com.github.elimxim

import com.github.elimxim.console.view.SortView
import com.github.elimxim.console.view.ArrayView
import com.github.elimxim.console.Console
import com.github.elimxim.console.view.ProbeView
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

        var maxExtraSize = 0
        val scene = script.scene()
        while (scene.isNotEmpty()) {
            delay(speedMillis)
            val frame = scene.poll()
            if (frame.extra.array.size > maxExtraSize) {
                maxExtraSize = frame.extra.array.size
            }
            printFrame(sortName, frame, refresh = true)
        }

        val ending = SortScriptImpl(probe, arrayWrapper)
        printEnding(sortName, ending, array, maxExtraSize)
    }

    private suspend fun printOpening(sortName: SortName, opening: SortScript, array: IntArray) {
        ArrayShuffler(opening).shuffle(array)
        val scene = opening.scene()
        printFrame(sortName, scene.poll())
        while (scene.isNotEmpty()) {
            delay(OPENING_DELAY)
            printFrame(sortName, scene.poll(), refresh = true)
        }
    }

    private suspend fun printEnding(sortName: SortName, ending: SortScript, array: IntArray, maxExtraSize: Int) {
        ending.line(Extra(array = IntArray(maxExtraSize)))

        for (i in array.indices) {
            ending.line(Focus(i))
        }
        val indexes = mutableSetOf<Int>()
        for (i in array.indices) {
            indexes.add(i)
            ending.line(Focus(*indexes.toIntArray()))
        }
        val scene = ending.scene()
        while (scene.isNotEmpty()) {
            delay(ENDING_DELAY)
            printFrame(sortName, scene.poll(), refresh = true)
        }
    }

    private fun printFrame(sortName: SortName, frame: Frame, refresh: Boolean = false) {
        val proveView = ProbeView(sortName, frame.probe)
        val arrayView = ArrayView(
                array = frame.main.array,
                focused = frame.main.focused,
                selected = frame.main.selected,
                extra = frame.extra.array,
                extraFocused = frame.extra.focused,
                extraSelected = frame.extra.selected
        )

        val lines = mutableListOf<String>()
        lines.addAll(arrayView.lines())
        lines.add("")
        lines.addAll(proveView.lines())
        Console.printLines(lines, refresh)
    }

    private companion object {
        const val OPENING_DELAY: Long = 400
        const val AFTER_OPENING_DELAY: Long = 1000
        const val ENDING_DELAY: Long = 50
    }
}