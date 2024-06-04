package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun focus(index: Int, variable: Int? = null)
    fun focus(pair: Pair<Int, Int>)
    fun focus(indexes: Set<Int>)
    fun focus(index: Int, override: IntArray)
    fun select(index: Int, variable: Int? = null)
    fun select(index: Int, override: IntArray)
    fun swap(pair: Pair<Int, Int>)
    fun swap(bulkSwap: BulkSwap, variable: Int? = null)
    fun bulkSwap(): BulkSwap
    fun deselect()
    fun screenplay(): Screenplay
}

interface Screenplay : Queue<ScriptLine>

class ScreenplayImpl(lines: List<ScriptLine>)
    : Screenplay, LinkedList<ScriptLine>(lines)

// not thread safe
class ScriptLine(
        val array: IntArray,
        val variable: Int? = null,
        val focused: Set<Int> = emptySet(),
        val selected: Set<Int> = emptySet(),
        val probeSnapshot: Probe.Snapshot
)

// not thread safe
class BulkSwap(val original: IntArray) {
    private val shifts: MutableList<Swap> = ArrayList()

    fun add(pair: Pair<Int, Int>) {
        shifts.add(Swap(pair.first, pair.second))
    }

    fun isNotEmpty(): Boolean {
        return shifts.isNotEmpty()
    }

    fun swaps(): List<Swap> {
        return shifts.toList()
    }
}

class Swap(val focused: Int, val selected: Int, val variable: Int? = null)

// not thread safe
class SortScriptImpl(
        private val probe: Probe,
        private val arrayWrapper: IntArrayWrapper
) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun focus(index: Int, variable: Int?) {
        focusInternal(arrayWrapper.original(), setOf(index), variable)
    }

    override fun focus(pair: Pair<Int, Int>) {
        focusInternal(arrayWrapper.original(), setOf(pair.first, pair.second))
    }

    override fun focus(indexes: Set<Int>) {
        focusInternal(arrayWrapper.original(), indexes)
    }

    override fun focus(index: Int, override: IntArray) {
        focusInternal(override, setOf(index))
    }

    override fun select(index: Int, variable: Int?) {
        selectInternal(arrayWrapper.original(), setOf(index), variable)
    }

    override fun select(index: Int, override: IntArray) {
        selectInternal(override, setOf(index))
    }

    override fun swap(pair: Pair<Int, Int>) {
        scriptLines.add(ScriptLine(
                array = arrayWrapper.original(),
                selected = setOf(pair.first, pair.second),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun swap(bulkSwap: BulkSwap, variable: Int?) {
        if (bulkSwap.isNotEmpty()) {
            val swaps = bulkSwap.swaps()

            val focused = swaps.map { it.focused }.toSet()
            focusInternal(bulkSwap.original, focused, variable)

            val selected = swaps.map { it.selected }.toSet()
            selectInternal(arrayWrapper.original(), selected, variable)
        }
    }

    override fun bulkSwap(): BulkSwap {
        return BulkSwap(arrayWrapper.original())
    }

    override fun deselect() {
        scriptLines.add(ScriptLine(
                array = arrayWrapper.original(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun screenplay(): Screenplay {
        return ScreenplayImpl(scriptLines)
    }

    private fun focusInternal(array: IntArray, indexes: Set<Int>, variable: Int? = null) {
        scriptLines.add(ScriptLine(
                array = array,
                variable = variable,
                focused = indexes,
                probeSnapshot = probe.snapshot()
        ))
    }

    private fun selectInternal(array: IntArray, indexes: Set<Int>, variable: Int? = null) {
        scriptLines.add(ScriptLine(
                array = array,
                variable = variable,
                selected = indexes,
                probeSnapshot = probe.snapshot()
        ))
    }
}

class NoOpSortScript : SortScript {
    override fun focus(index: Int, variable: Int?) {
    }

    override fun focus(pair: Pair<Int, Int>) {
    }

    override fun focus(indexes: Set<Int>) {
    }

    override fun focus(index: Int, override: IntArray) {
    }

    override fun select(index: Int, variable: Int?) {
    }

    override fun select(index: Int, override: IntArray) {
    }

    override fun swap(pair: Pair<Int, Int>) {
    }

    override fun swap(bulkSwap: BulkSwap, variable: Int?) {
    }

    override fun bulkSwap(): BulkSwap {
        return BulkSwap(IntArray(0))
    }

    override fun deselect() {
    }

    override fun screenplay(): Screenplay {
        throw UnsupportedOperationException("no op")
    }
}