package com.github.elimxim

import com.github.elimxim.view.SortView
import com.github.elimxim.view.ArrayView
import com.github.elimxim.console.Console
import com.github.elimxim.view.ProbeView
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
            Console.printLines(sortView.lines())
            Console.printEmptyLine()
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

        ScriptPrinter(
                FramePrinter(sortName, arrayLength),
                openingBeat = 400,
                beforeSceneDelay = 1000,
                sceneBeast = speedMillis,
                afterSceneDelay = 100,
                endingBeat = 50
        ).print(
                opening = {
                    val script = SortScriptImpl(probe, arrayWrapper)
                    ArrayShuffler(script).shuffle(array)
                    script
                },
                script = {
                    val script = SortScriptImpl(probe, arrayWrapper)
                    val sort = SortFactory.instance(sortName, probe, script)
                    sort.sort(arrayWrapper)
                    script
                },
                ending = {
                    val script = SortScriptImpl(probe, arrayWrapper)
                    script.action(Nothing)
                    for (i in 0..<arrayWrapper.size()) {
                        script.action(Focus(i))
                    }
                    val indexes = mutableSetOf<Int>()
                    for (i in 0..<arrayWrapper.size()) {
                        indexes.add(i)
                        script.action(Focus(*indexes.toIntArray()))
                    }
                    script
                }
        )
    }
}

class ScriptPrinter(
        private val framePrinter: FramePrinter,
        private val openingBeat: Long,
        private val beforeSceneDelay: Long,
        private val sceneBeast: Long,
        private val afterSceneDelay: Long,
        private val endingBeat: Long
) {
    private var maxExtraSize: Int = 0

    suspend fun print(
            opening: () -> SortScript,
            script: () -> SortScript,
            ending: () -> SortScript
    ) {
        val openingRecord = opening().record()
        printFrame(openingRecord.poll(), refresh = false)
        while (openingRecord.isNotEmpty()) {
            delay(openingBeat)
            printFrame(openingRecord.poll(), refresh = true)
        }

        delay(beforeSceneDelay)

        val record = script().record()
        while (record.isNotEmpty()) {
            delay(sceneBeast)
            val frame = record.poll()
            printFrame(frame, refresh = true)
        }

        delay(afterSceneDelay)

        val endingRecord = ending().record()
        while (endingRecord.isNotEmpty()) {
            delay(endingBeat)
            printFrame(endingRecord.poll(), refresh = true)
        }
    }

    private fun printFrame(shot: Shot, refresh: Boolean) {
        val extraSize = shot.extraScreen.array.size
        if (extraSize >= maxExtraSize) {
            maxExtraSize = extraSize
            framePrinter.print(shot, refresh)
        } else {
            framePrinter.print(adjustFrame(shot, maxExtraSize), refresh)
            maxExtraSize = extraSize
        }
    }

    private fun adjustFrame(shot: Shot, extraSize: Int): Shot {
        val extraArray = IntArray(extraSize)
        shot.extraScreen.array.copyInto(extraArray)

        return Shot(
                mainScreen = shot.mainScreen,
                extraScreen = Screen(
                        array = extraArray,
                        focused = shot.extraScreen.focused,
                        selected = shot.extraScreen.selected
                ),
                probeSnapshot = shot.probeSnapshot
        )
    }
}

class FramePrinter(
        private val sortName: SortName,
        private val screenHeight: Int
) {
    fun print(shot: Shot, refresh: Boolean = false) {
        val probeView = ProbeView(sortName, shot.probeSnapshot)
        val arrayView = ArrayView(
                array = shot.mainScreen.array,
                focused = shot.mainScreen.focused,
                selected = shot.mainScreen.selected,
                extra = shot.extraScreen.array,
                extraFocused = shot.extraScreen.focused,
                extraSelected = shot.extraScreen.selected,
                height = screenHeight
        )

        val lines = mutableListOf<String>()
        lines.addAll(arrayView.lines())
        lines.add("")
        lines.addAll(probeView.lines())
        Console.printLines(lines, refresh)
    }
}