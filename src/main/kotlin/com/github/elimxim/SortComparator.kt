package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.view.ProbeView
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.time.Duration
import kotlin.time.TimeSource

class SortComparator(
        private val arrayLength: Int,
        private val arrayFile: Path,
        private val printArray: Boolean,
        private val showInfo: Boolean
) {
    fun compare(sortNames: List<SortName>) {
        if (showInfo) {
            SortInfoPrinter().print(sortNames)
        }

        val array = (1..arrayLength).toIntArray()
        ArrayShuffler().shuffle(array)

        if (printArray) {
            var outputFile = arrayFile
            if (!arrayFile.name.endsWith(ARRAY_FILE_EXT)) {
                val newName = arrayFile.name.withTimestamp(ARRAY_FILE_POSTFIX).plus(ARRAY_FILE_EXT)
                outputFile = arrayFile.parent.resolve(newName)
            }
            ArrayPrinter(outputFile).printArray(array)
        }

        doCompare(sortNames, array)
    }

    private fun doCompare(sortNames: List<SortName>, array: IntArray) {
        Console.printLine("array size: ${array.size}")

        val sortProbes = sortNames
                .sortedBy { it.ordinal }
                .map { Pair(it, Probe()) }

        runBlocking {
            withContext(Dispatchers.Default) {
                val timeMark = TimeSource.Monotonic.markNow()
                val jobs = sortProbes.map { sortProbe ->
                    launch {
                        val arrayWrapper = IntArrayWrapper(array.copyOf(), sortProbe.second)
                        SortFactory.instance(sortProbe.first, sortProbe.second).sort(arrayWrapper)
                    }
                }

                launch {
                    printProbes(timeMark.elapsedNow(), sortProbes)
                    while (!isCompleted(jobs)) {
                        delay(250)
                        printProbes(timeMark.elapsedNow(), sortProbes, refresh = true)
                    }
                    printProbes(timeMark.elapsedNow(), sortProbes, refresh = true)
                }
            }
        }
    }

    private fun isCompleted(jobs: List<Job>): Boolean {
        return jobs.all { job -> job.isCompleted }
    }

    private fun printProbes(elapsedTime: Duration, sortProbes: List<Pair<SortName, Probe>>, refresh: Boolean = false) {
        val lines = mutableListOf<String>()
        lines.add("elapsed time: ${elapsedTime.inWholeMilliseconds} ms")
        sortProbes.forEach {
            lines.add("")
            lines.addAll(ProbeView(it.first, it.second.snapshot()).lines())
        }

        Console.printLines(lines.toList(), refresh)
    }

    companion object {
        const val ARRAY_FILE_POSTFIX = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val ARRAY_FILE_EXT = ".txt"
    }
}