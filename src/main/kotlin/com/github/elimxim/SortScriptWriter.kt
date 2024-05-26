package com.github.elimxim

import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScriptWriter {
    fun focus(array: ArrayWrapper<Int>, index: Int)
    fun replace(array: ArrayWrapper<Int>, index1: Int, index2: Int)
    fun scriptLines(): Queue<ScriptLine>
}

class ScriptLine(
        val array: Array<Int>,
        val focus: Int,
        val probeSnapshot: Probe.Snapshot
)

// not thread safe
class SortScriptWriterImpl(private val probe: Probe) : SortScriptWriter {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun focus(array: ArrayWrapper<Int>, index: Int) {
        scriptLines.add(ScriptLine(array.array(), index, probe.snapshot()))
    }

    override fun replace(array: ArrayWrapper<Int>, index1: Int, index2: Int) {
        scriptLines.add(ScriptLine(array.array(), index2, probe.snapshot()))
    }

    override fun scriptLines(): Queue<ScriptLine> {
        return LinkedList(scriptLines)
    }
}

class NoOpSortScriptWriter : SortScriptWriter {
    override fun focus(array: ArrayWrapper<Int>, index: Int) {
    }

    override fun replace(array: ArrayWrapper<Int>, index1: Int, index2: Int) {
    }

    override fun scriptLines(): Queue<ScriptLine> {
        throw UnsupportedOperationException("no op")
    }
}