package com.github.elimxim

import java.util.concurrent.atomic.AtomicLong

class Probe {
    private var iterations = AtomicLong()
    private var comparisons = AtomicLong()
    private var arrayReads = AtomicLong()
    private var arrayWrites = AtomicLong()
    private var arraySwaps = AtomicLong()

    enum class Counter {
        ITERATIONS,
        COMPARISONS,
        ARRAY_READS,
        ARRAY_WRITES,
        ARRAY_SWAPS
    }

    fun increment(vararg counters: Counter) {
        counters.forEach { counter ->
            getCounter(counter).incrementAndGet()
        }
    }

    fun decrement(vararg counters: Counter) {
        counters.forEach { counter ->
            getCounter(counter).decrementAndGet()
        }
    }

    private fun getCounter(counter: Counter): AtomicLong {
        return when (counter) {
            Counter.ITERATIONS -> iterations
            Counter.COMPARISONS -> comparisons
            Counter.ARRAY_READS -> arrayReads
            Counter.ARRAY_WRITES -> arrayWrites
            Counter.ARRAY_SWAPS -> arraySwaps
        }
    }

    fun snapshot(): Snapshot {
        return Snapshot(
                iterations = iterations.get(),
                comparisons = comparisons.get(),
                arrayReads = arrayReads.get(),
                arrayWrites = arrayWrites.get(),
                arraySwaps = arraySwaps.get()
        )
    }

    class Snapshot(
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