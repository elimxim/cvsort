package com.github.elimxim

import com.github.elimxim.console.ConsolePrinter
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.time.Duration
import kotlin.time.TimeSource

class SortComparator(
        private val arrayFile: Path,
        private val printArray: Boolean,
        private val showInfo: Boolean
) {
    fun compare(algorithms: List<Algorithm>, array: Array<Int>) {
        if (printArray) {
            ArrayPrinter(arrayFile).printArray(array)
        }

        if (showInfo) {
            val table = AlgorithmComplexity()
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
                        val arrayWrapper = ArrayProbingWrapper(array.copyOf(), probe)
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
            lines.add("<${snap.algorithm.canonicalName()}>")
            lines.add(" iterations   ${snap.iterations}  comparisons  ${snap.comparisons}")
            lines.add(" array reads/writes  ${snap.arrayReads}/${snap.arrayWrites}  ratio  ${"%.2f".format(snap.arrayRatio)}")
            lines.add(" array swaps  ${snap.arraySwaps}")
        }

        ConsolePrinter.printLines(lines.toList(), refresh)
    }
}