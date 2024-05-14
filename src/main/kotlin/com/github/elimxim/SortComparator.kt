package com.github.elimxim

import com.github.elimxim.ComplexityClass.*
import com.github.elimxim.console.ConsolePrinter
import de.vandermeer.asciitable.AsciiTable
import kotlinx.coroutines.*
import java.nio.file.Path
import kotlin.reflect.full.findAnnotation
import kotlin.time.Duration
import kotlin.time.TimeSource

class SortComparator(
        private val arrayFile: Path,
        private val printArray: Boolean,
        private val showInfo: Boolean
) {
    fun compare(algorithms: List<Algorithm>, array: Array<Int>) {
        if (printArray) {
            printArray(array)
        }

        if (showInfo) {
            printInfo(algorithms)
        }

        val probes = algorithms
                .sortedBy { it.ordinal }
                .map { Probe(it) }

        runBlocking {
            withContext(Dispatchers.Default) {
                val timeMark = TimeSource.Monotonic.markNow()
                val jobs = probes.map { probe ->
                    launch {
                        val testArray = TestArray(array.copyOf(), probe)
                        SortFactory.instance(probe.algorithm, probe).sort(testArray)
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

    private fun printArray(array: Array<Int>) {
        ArrayPrinter(arrayFile).printArray(array)
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

    private fun printInfo(algorithms: List<Algorithm>) {
        val content = algorithms.mapNotNull {
            val impl = SortFactory.impl(it)
            val anno = impl.findAnnotation<SortAlgorithm>()

            if (anno != null) {
                listOf(
                        it.canonicalName(),
                        complexity(anno.timeComplexity.worst, BIG_O),
                        complexity(anno.timeComplexity.average, BIG_THETA),
                        complexity(anno.timeComplexity.best, BIG_OMEGA),
                        complexity(anno.spaceComplexity, BIG_O)
                )
            } else {
                null
            }
        }

        if (content.size > 1) {
            val table = AsciiTable()

            table.addRule()
            table.addRow(mutableListOf(
                    "Algorithm",
                    "Worst time",
                    "Average time",
                    "Best time",
                    "Space"
            ))
            table.addRule()
            content.forEach { table.addRow(it) }
            table.addRule()

            ConsolePrinter.printLines(table.renderAsCollection().toList())
            ConsolePrinter.printSpaceLine()
        }
    }
}