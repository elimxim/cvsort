package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun focus(array: IntArrayWrapper, index: Int)
    fun replace(array: IntArrayWrapper, index1: Int, index2: Int)
    fun scriptLines(): Queue<ScriptLine>
    fun focusEvery(array: IntArrayWrapper)
    fun focusAll(array: IntArrayWrapper)
}

class ScriptLine(
        val array: IntArray,
        val focus: Int,
        val probeSnapshot: Probe.Snapshot
) {
    companion object {
        const val FOCUS_ALL = -1
    }
}

// not thread safe
class SortScriptImpl(private val probe: Probe) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()
    private var focus: Int = 0

    override fun focus(array: IntArrayWrapper, index: Int) {
        if (focus != index) {
            scriptLines.add(ScriptLine(array.array(), index, probe.snapshot()))
        }
    }

    override fun replace(array: IntArrayWrapper, index1: Int, index2: Int) {
        scriptLines.add(ScriptLine(array.array(), index2, probe.snapshot()))
        focus = index2
    }

    override fun focusEvery(array: IntArrayWrapper) {
        val original = array.array()
        for (i in original.indices) {
            focus(array, i)
        }
    }

    override fun focusAll(array: IntArrayWrapper) {
        focus(array, ScriptLine.FOCUS_ALL)
    }

    override fun scriptLines(): Queue<ScriptLine> {
        return LinkedList(scriptLines)
    }
}

class NoOpSortScript : SortScript {
    override fun focus(array: IntArrayWrapper, index: Int) {
    }

    override fun replace(array: IntArrayWrapper, index1: Int, index2: Int) {
    }

    override fun scriptLines(): Queue<ScriptLine> {
        throw UnsupportedOperationException("no op")
    }

    override fun focusEvery(array: IntArrayWrapper) {
        throw UnsupportedOperationException("no op")
    }

    override fun focusAll(array: IntArrayWrapper) {
        throw UnsupportedOperationException("no op")
    }
}