package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun focus(index: Int)
    fun focus(indexes: Set<Int>, variable: Int? = null)
    fun select(index: Int)
    fun select(indexes: Set<Int>, variable: Int? = null)
    fun replace(bulkReplace: BulkReplace, variable: Int? = null)
    fun bulkReplace(array: IntArray): BulkReplace
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
class BulkReplace(val original: IntArray) {
    private val shifts: MutableList<Pair> = ArrayList()

    fun replace(focused: Int, selected: Int) {
        shifts.add(Pair(focused, selected))
    }

    fun isNotEmpty(): Boolean {
        return shifts.isNotEmpty()
    }

    fun actionPairs(): List<Pair> {
        return shifts.toList()
    }

    class Pair(val focused: Int, val selected: Int)
}

// not thread safe
class SortScriptImpl(
        private val probe: Probe,
        private val arrayWrapper: IntArrayWrapper
) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun focus(index: Int) {
        focus(setOf(index))
    }

    override fun focus(indexes: Set<Int>, variable: Int?) {
        scriptLines.add(ScriptLine(
                array = arrayWrapper.original(),
                variable = variable,
                focused = indexes,
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun select(index: Int) {
        select(setOf(index), null)
    }

    override fun select(indexes: Set<Int>, variable: Int?) {
        scriptLines.add(ScriptLine(
                array = arrayWrapper.original(),
                variable = variable,
                selected = indexes,
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun replace(bulkReplace: BulkReplace, variable: Int?) {
        if (bulkReplace.isNotEmpty()) {
            val shifts = bulkReplace.actionPairs()
            scriptLines.add(ScriptLine(
                    array = bulkReplace.original,
                    variable = variable,
                    focused = shifts.map { it.focused }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
            scriptLines.add(ScriptLine(
                    array = arrayWrapper.original(),
                    variable = variable,
                    selected = shifts.map { it.selected }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
        }
    }

    override fun bulkReplace(array: IntArray): BulkReplace {
        return BulkReplace(array)
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
}

class NoOpSortScript : SortScript {
    override fun focus(index: Int) {
    }

    override fun focus(indexes: Set<Int>, variable: Int?) {
    }

    override fun select(index: Int) {
    }

    override fun select(indexes: Set<Int>, variable: Int?) {
    }

    override fun replace(bulkReplace: BulkReplace, variable: Int?) {
    }

    override fun bulkReplace(array: IntArray): BulkReplace {
        return BulkReplace(array)
    }

    override fun deselect() {
    }

    override fun screenplay(): Screenplay {
        throw UnsupportedOperationException("no op")
    }
}