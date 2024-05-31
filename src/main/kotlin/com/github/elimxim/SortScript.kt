package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun focus(array: IntArray, index: Int)
    fun focus(array: IntArray, indexes: Set<Int>)
    fun focusLoop(array: IntArray)
    fun focusLoopAccumulative(array: IntArray)
    fun focusAll(array: IntArray)
    fun noFocus(array: IntArray)
    fun select(array: IntArray, index: Int)
    fun select(array: IntArray, index1: Int, index2: Int)
    fun select(array: IntArray, bulkSelection: BulkSelection)
    fun bulkSelection(array: IntArray): BulkSelection
    fun beginShift(array: IntArray): RightShift
    fun commitShift(shift: RightShift)
    fun scriptLines(): Queue<ScriptLine>
}

class ScriptLine(
        val array: IntArray,
        val focused: Set<Int> = emptySet(),
        val selected: Set<Int> = emptySet(),
        val probeSnapshot: Probe.Snapshot
)

class Selection(val array: IntArray, val focused: Int, val selected: Int)

class BulkSelection(val arrayBefore: IntArray) {
    private val selections: MutableList<Selection> = ArrayList()

    fun add(selection: Selection) {
        selections.add(selection)
    }

    fun isNotEmpty(): Boolean {
        return selections.isNotEmpty()
    }

    fun selections(): List<Selection> {
        return selections.toList()
    }
}

class RightShift(
        val original: IntArray,
        val number: Int = 1
) {
    lateinit var result: IntArray
    private val indexes: MutableSet<Int> = hashSetOf()

    fun addIndex(index: Int) {
        indexes.add(index)
    }

    fun happened(): Boolean {
        return indexes.isNotEmpty()
    }

    fun indexes(): Set<Int> {
        return indexes.toSet()
    }
}

// not thread safe
class SortScriptImpl(private val probe: Probe) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun focus(array: IntArray, index: Int) {
        scriptLines.add(ScriptLine(
                array = array,
                focused = setOf(index),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun focus(array: IntArray, indexes: Set<Int>) {
        scriptLines.add(ScriptLine(
                array = array,
                focused = indexes,
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun focusLoop(array: IntArray) {
        for (i in array.indices) {
            focus(array, i)
        }
    }

    override fun focusLoopAccumulative(array: IntArray) {
        val indexes = mutableSetOf<Int>()
        for (i in array.indices) {
            indexes.add(i)
            scriptLines.add(ScriptLine(
                    array = array,
                    focused = indexes.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
        }
    }

    override fun focusAll(array: IntArray) {
        scriptLines.add(ScriptLine(
                array = array,
                focused = (array.indices).toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun noFocus(array: IntArray) {
        scriptLines.add(ScriptLine(
                array = array,
                focused = emptySet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun select(array: IntArray, index: Int) {
        scriptLines.add(ScriptLine(
                array = array,
                selected = setOf(index),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun select(array: IntArray, index1: Int, index2: Int) {
        scriptLines.add(ScriptLine(
                array = array,
                selected = setOf(index1, index2),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun select(array: IntArray, bulkSelection: BulkSelection) {
        if (bulkSelection.isNotEmpty()) {
            val selections = bulkSelection.selections()
            scriptLines.add(ScriptLine(
                    array = bulkSelection.arrayBefore,
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

    override fun bulkSelection(array: IntArray): BulkSelection {
        return BulkSelection(array)
    }

    override fun beginShift(array: IntArray): RightShift {
        return RightShift(original = array)
    }

    override fun commitShift(shift: RightShift) {
        scriptLines.add(ScriptLine(
                array = shift.original,
                focused = shift.indexes(),
                probeSnapshot = probe.snapshot()
        ))
        scriptLines.add(ScriptLine(
                array = shift.result,
                selected = shift.indexes().map { it + shift.number }.toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun scriptLines(): Queue<ScriptLine> {
        return LinkedList(scriptLines)
    }
}

class NoOpSortScript : SortScript {
    override fun focus(array: IntArray, index: Int) {
    }

    override fun focus(array: IntArray, indexes: Set<Int>) {
    }

    override fun focusLoop(array: IntArray) {
    }

    override fun focusLoopAccumulative(array: IntArray) {
    }

    override fun focusAll(array: IntArray) {
    }

    override fun noFocus(array: IntArray) {
    }

    override fun select(array: IntArray, index: Int) {
    }

    override fun select(array: IntArray, index1: Int, index2: Int) {
    }

    override fun select(array: IntArray, bulkSelection: BulkSelection) {
    }

    override fun bulkSelection(array: IntArray): BulkSelection {
        return BulkSelection(array)
    }

    override fun beginShift(array: IntArray): RightShift {
        return RightShift(array)
    }

    override fun commitShift(shift: RightShift) {
    }

    override fun scriptLines(): Queue<ScriptLine> {
        throw UnsupportedOperationException("no op")
    }
}