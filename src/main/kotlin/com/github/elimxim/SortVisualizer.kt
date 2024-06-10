package com.github.elimxim

import com.github.elimxim.console.view.SortView
import com.github.elimxim.console.view.ArrayView
import com.github.elimxim.console.Console
import com.github.elimxim.console.view.ProbeView
import kotlinx.coroutines.*

// not thread safe
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

        ScriptPrinter(
                FramePrinter(sortName, arrayLength),
                openingFrameDelay = 400,
                beforeSceneDelay = 1000,
                sceneFrameDelay = speedMillis,
                afterSceneDelay = 100,
                endingFrameDelay = 50
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
                    script.line(Nothing)
                    for (i in 0..<arrayWrapper.size()) {
                        script.line(Focus(i))
                    }
                    val indexes = mutableSetOf<Int>()
                    for (i in 0..<arrayWrapper.size()) {
                        indexes.add(i)
                        script.line(Focus(*indexes.toIntArray()))
                    }
                    script
                }
        )
    }
}

// not thread safe
class ScriptPrinter(
        private val framePrinter: FramePrinter,
        private val openingFrameDelay: Long,
        private val beforeSceneDelay: Long,
        private val sceneFrameDelay: Long,
        private val afterSceneDelay: Long,
        private val endingFrameDelay: Long
) {
    private var maxExtraSize: Int = 0

    suspend fun print(
            opening: () -> SortScript,
            script: () -> SortScript,
            ending: () -> SortScript
    ) {
        val openingScene = opening().scene()
        printFrame(openingScene.poll(), refresh = false)
        while (openingScene.isNotEmpty()) {
            delay(openingFrameDelay)
            printFrame(openingScene.poll(), refresh = true)
        }

        delay(beforeSceneDelay)

        val scriptScene = script().scene()
        while (scriptScene.isNotEmpty()) {
            delay(sceneFrameDelay)
            val frame = scriptScene.poll()
            printFrame(frame, refresh = true)
        }

        delay(afterSceneDelay)

        val endingScene = ending().scene()
        while (endingScene.isNotEmpty()) {
            delay(endingFrameDelay)
            printFrame(endingScene.poll(), refresh = true)
        }
    }

    private fun printFrame(frame: Frame, refresh: Boolean) {
        val extraSize = frame.extraData.array.size
        if (extraSize >= maxExtraSize) {
            maxExtraSize = extraSize
            framePrinter.print(frame, refresh)
        } else {
            framePrinter.print(adjustFrame(frame, maxExtraSize), refresh)
            maxExtraSize = extraSize
        }
    }

    private fun adjustFrame(frame: Frame, extraSize: Int): Frame {
        val extraArray = IntArray(extraSize)
        frame.extraData.array.copyInto(extraArray)

        return Frame(
                mainData = frame.mainData,
                extraData = Data(
                        array = extraArray,
                        focused = frame.extraData.focused,
                        selected = frame.extraData.selected
                ),
                probeSnapshot = frame.probeSnapshot
        )
    }
}

// not thread safe
class FramePrinter(
        private val sortName: SortName,
        private val screenHeight: Int
) {
    fun print(frame: Frame, refresh: Boolean = false) {
        val probeView = ProbeView(sortName, frame.probeSnapshot)
        val arrayView = ArrayView(
                array = frame.mainData.array,
                focused = frame.mainData.focused,
                selected = frame.mainData.selected,
                extra = frame.extraData.array,
                extraFocused = frame.extraData.focused,
                extraSelected = frame.extraData.selected,
                height = screenHeight
        )

        val lines = mutableListOf<String>()
        lines.addAll(arrayView.lines())
        lines.add("")
        lines.addAll(probeView.lines())
        Console.printLines(lines, refresh)
    }
}