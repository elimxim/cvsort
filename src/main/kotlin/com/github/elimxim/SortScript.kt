package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun focus(array: IntArray, vararg indexes: Int, variable: Int? = null)
    fun select(array: IntArray, vararg indexes: Int, variable: Int? = null)
    fun change(array: IntArray, focused: Int, selected: Int)
    fun change(array: IntArray, bulkChange: BulkChange)
    fun bulkChange(array: IntArray): BulkChange
    fun discard(array: IntArray)
    fun scriptLines(): Queue<ScriptLine>
}

class ScriptLine(
        val array: IntArray,
        val variable: Int? = null,
        val focused: Set<Int> = emptySet(),
        val selected: Set<Int> = emptySet(),
        val probeSnapshot: Probe.Snapshot
)

class Change(val focused: Int, val selected: Int)

class BulkChange(val arrayBefore: IntArray) {
    private val changes: MutableList<Change> = ArrayList()

    fun add(change: Change) {
        changes.add(change)
    }

    fun isNotEmpty(): Boolean {
        return changes.isNotEmpty()
    }

    fun changes(): List<Change> {
        return changes.toList()
    }
}

// not thread safe
class SortScriptImpl(private val probe: Probe) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun focus(array: IntArray, vararg indexes: Int, variable: Int?) {
        scriptLines.add(ScriptLine(
                array = array,
                variable = variable,
                focused = indexes.toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun select(array: IntArray, vararg indexes: Int, variable: Int?) {
        scriptLines.add(ScriptLine(
                array = array,
                variable = variable,
                selected = indexes.toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun change(array: IntArray, focused: Int, selected: Int) {
        scriptLines.add(ScriptLine(
                array = array,
                focused = setOf(focused),
                selected = setOf(selected),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun change(array: IntArray, bulkChange: BulkChange) {
        if (bulkChange.isNotEmpty()) {
            val selections = bulkChange.changes()
            scriptLines.add(ScriptLine(
                    array = bulkChange.arrayBefore,
                    focused = selections.map { it.focused }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
            scriptLines.add(ScriptLine(
                    array = array,
                    selected = selections.map { it.selected }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
        }
    }

    override fun bulkChange(array: IntArray): BulkChange {
        return BulkChange(array)
    }

    override fun discard(array: IntArray) {
        scriptLines.add(ScriptLine(
                array = array,
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun scriptLines(): Queue<ScriptLine> {
        return LinkedList(scriptLines)
    }
}

class NoOpSortScript : SortScript {
    override fun focus(array: IntArray, vararg indexes: Int, variable: Int?) {
    }

    override fun select(array: IntArray, vararg indexes: Int, variable: Int?) {
    }

    override fun change(array: IntArray, focused: Int, selected: Int) {
    }

    override fun change(array: IntArray, bulkChange: BulkChange) {
    }

    override fun bulkChange(array: IntArray): BulkChange {
        return BulkChange(array)
    }

    override fun discard(array: IntArray) {
    }

    override fun scriptLines(): Queue<ScriptLine> {
        throw UnsupportedOperationException("no op")
    }
}