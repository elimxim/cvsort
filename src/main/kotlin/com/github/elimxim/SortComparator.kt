package com.github.elimxim

import com.github.elimxim.console.AlgorithmView
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.ProbeView
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.TimeSource

class SortComparator(
        private val arrayFile: Path,
        private val printArray: Boolean,
        private val showInfo: Boolean,
        private val arrayLength: Int
) {
    fun compare(algorithms: List<Algorithm>) {
        val array = ArrayGenerator.generate(1, arrayLength)

        if (printArray) {
            ArrayPrinter(arrayFile).printArray(array)
        }

        if (showInfo) {
            val table = AlgorithmView()
            algorithms.forEach(table::add)
            table.print()
        }

        ConsolePrinter.printLine("array size: ${array.size}")

        val probes = algorithms
                .sortedBy { it.ordinal }
                .map { Probe(it) }

        runBlocking {
            withContext(Dispatchers.Default) {
                val timeMark = TimeSource.Monotonic.markNow()
                val jobs = probes.map { probe ->
                    launch {
                        val arrayWrapper = IntArrayWrapper(array.copyOf(), probe, NoOpSortScript())
                        SortFactory.instance(probe.algorithm, probe).sort(arrayWrapper)
                    }
                }

                launch {
                    printProbes(timeMark.elapsedNow(), probes.map { it.snapshot() })
                    while (!isCompleted(jobs)) {
                        delay(250)
                        printProbes(timeMark.elapsedNow(), probes.map { it.snapshot() }, refresh = true)
                    }
                    printProbes(timeMark.elapsedNow(), probes.map { it.snapshot() }, refresh = true)
                }
            }
        }
    }

    private fun isCompleted(jobs: List<Job>): Boolean {
        return jobs.all { job -> job.isCompleted }
    }

    private fun printProbes(elapsedTime: Duration, snaps: List<Probe.Snapshot>, refresh: Boolean = false) {
        val lines = mutableListOf<String>()
        lines.add("elapsed time: ${elapsedTime.inWholeMilliseconds} ms")
        snaps.forEach { snap ->
            lines.add("")
            lines.addAll(ProbeView(snap).lines())
        }

        ConsolePrinter.printLines(lines.toList(), refresh)
    }
}