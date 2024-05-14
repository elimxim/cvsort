package com.github.elimxim

import java.util.concurrent.atomic.AtomicLong

class Probe(val algorithm: Algorithm) {
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
                algorithm = algorithm,
                iterations = iterations.get(),
                comparisons = comparisons.get(),
                arrayReads = arrayReads.get(),
                arrayWrites = arrayWrites.get(),
                arraySwaps = arraySwaps.get()
        )
    }

    class Snapshot(
            val algorithm: Algorithm,
            val iterations: Long,
            val comparisons: Long,
            val arrayReads: Long,
            val arrayWrites: Long,
            val arraySwaps: Long,
    ) {
        val arrayRatio: Double = (arrayReads.toDouble() / arrayWrites.toDouble())
    }
}