package com.github.elimxim

import java.util.concurrent.atomic.AtomicLong

class Probe(val sortName: SortName) {
    private var iterations = AtomicLong()
    private var comparisons = AtomicLong()
    private var arrayReads = AtomicLong()
    private var arrayWrites = AtomicLong()
    private var arraySwaps = AtomicLong()

    enum class Increment {
        ITERATIONS,
        COMPARISONS,
        ARRAY_READS,
        ARRAY_WRITES,
        ARRAY_SWAPS
    }

    fun increment(vararg increments: Increment) {
        increments.forEach { increment ->
            when (increment) {
                Increment.ITERATIONS -> iterations.incrementAndGet()
                Increment.COMPARISONS -> comparisons.incrementAndGet()
                Increment.ARRAY_READS -> arrayReads.incrementAndGet()
                Increment.ARRAY_WRITES -> arrayWrites.incrementAndGet()
                Increment.ARRAY_SWAPS -> arraySwaps.incrementAndGet()
            }
        }
    }

    fun snapshot(): Snapshot {
        return Snapshot(
                sortName = sortName,
                iterations = iterations.get(),
                comparisons = comparisons.get(),
                arrayReads = arrayReads.get(),
                arrayWrites = arrayWrites.get(),
                arraySwaps = arraySwaps.get()
        )
    }

    class Snapshot(
            val sortName: SortName,
            val iterations: Long,
            val comparisons: Long,
            val arrayReads: Long,
            val arrayWrites: Long,
            val arraySwaps: Long,
    ) {
        fun arrayRatio(): Double = if (arrayReads != 0L && arrayWrites != 0L) {
            arrayReads.toDouble() / arrayWrites.toDouble()
        } else {
            0.0
        }
    }
}