package com.github.elimxim

import com.github.elimxim.view.SortClassificationView
import com.github.elimxim.view.ArrayView
import com.github.elimxim.console.Console
import com.github.elimxim.view.ProbeView
import kotlinx.coroutines.*
import kotlin.math.abs

class SortVisualizer(
        private val frameDelayMillis: Long,
        private val arrayLength: Int,
        private val showShuffle: Boolean,
        private val showInfo: Boolean,
        private val reverse: Boolean,
        private val casualMode: Boolean
) {
    fun visualize(sortName: SortName) {
        if (showInfo) {
            val sortView = SortClassificationView()
            val anno = SortFactory.classification(sortName)
            if (anno != null) {
                sortView.add(sortName, anno)
            }
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
                scriptBeat = abs(frameDelayMillis),
                actDelay = 500,
                endingBeat = 50,
                showOpening = showShuffle,
                backwards = reverse,
                casualMode = casualMode
        ).print(
                opening = {
                    val script = SortScriptImpl(probe, arrayWrapper)
                    script.action(Nothing)
                    ArrayShuffler(script).shuffle(array)
                    script.action(Nothing)
                    script
                },
                script = {
                    val script = SortScriptImpl(probe, arrayWrapper)
                    val sort = SortFactory.instance(sortName, probe, script)
                    sort.sort(arrayWrapper)
                    script.action(Nothing)
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
        private val scriptBeat: Long,
        private val actDelay: Long,
        private val endingBeat: Long,
        private val showOpening: Boolean,
        private val backwards: Boolean,
        private val casualMode: Boolean
) {
    private var maxExtraSize: Int = 0

    suspend fun print(
            opening: () -> SortScript,
            script: () -> SortScript,
            ending: () -> SortScript
    ) {
        val openingRecord = opening().record()
        val scriptRecord = script().record()
        val endingRecord = ending().record()

        val switch = Switch(false)
        if (backwards.not()) {
            if (showOpening) {
                playRecord(openingRecord, openingBeat, switch)
                delay(actDelay)
            }
            playRecord(scriptRecord, scriptBeat, switch, cutShot = casualMode)
            delay(actDelay)
            playRecord(endingRecord, endingBeat, switch)
        } else {
            playRecord(endingRecord, endingBeat, switch)
            delay(actDelay)
            playRecord(scriptRecord, scriptBeat, switch, cutShot = casualMode)
        }
    }

    private suspend fun playRecord(record: Record, beat: Long, switch: Switch, cutShot: Boolean = false) {
        while (record.isNotEmpty()) {
            val shot = if (backwards.not()) {
                record.pollFirst()
            } else {
                record.pollLast()
            }

            if (cutShot) {
                val main = shot.mainScreen
                if (main.selected.isNotEmpty() || main.focused.isEmpty()) {
                    delay(beat)
                    printFrame(cutShot(shot), switch.value())
                }
            } else {
                delay(beat)
                printFrame(shot, switch.value())
            }
        }
    }

    private fun printFrame(shot: Shot, refresh: Boolean) {
        val extraSize = shot.extraScreen.array.size
        if (extraSize >= maxExtraSize) {
            maxExtraSize = extraSize
            framePrinter.print(shot, refresh)
        } else {
            framePrinter.print(adjustShot(shot, maxExtraSize), refresh)
            maxExtraSize = extraSize
        }
    }

    private fun adjustShot(shot: Shot, extraSize: Int): Shot {
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

    private fun cutShot(shot: Shot): Shot {
        return Shot(
                mainScreen = Screen(
                        array = shot.mainScreen.array,
                        selected = shot.mainScreen.selected
                ),
                extraScreen = Screen(emptyArray<Int>().toIntArray()),
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

class Switch(private val initial: Boolean) {
    private var value: Boolean = initial

    fun value(): Boolean {
        if (value.xnor(initial)) {
            value = initial.not()
            return initial
        }
        return value
    }
}