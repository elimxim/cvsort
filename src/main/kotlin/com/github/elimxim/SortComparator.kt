package com.github.elimxim

import com.github.elimxim.console.SortView
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.ProbeView
import kotlinx.coroutines.*
import java.nio.file.Path
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
            val sortView = SortView()
            sortNames.distinct().forEach(sortView::add)
            sortView.print()
        }

        val array = (1..arrayLength).toIntArray()
        ArrayShuffler().shuffle(array)

        if (printArray) {
            ArrayPrinter(arrayFile).printArray(array)
        }

        doCompare(sortNames, array)
    }

    private fun doCompare(sortNames: List<SortName>, array: IntArray) {
        ConsolePrinter.printLine("array size: ${array.size}")

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

        ConsolePrinter.printLines(lines.toList(), refresh)
    }
}