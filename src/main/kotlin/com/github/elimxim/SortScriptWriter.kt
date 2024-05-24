package com.github.elimxim

import java.lang.RuntimeException

// not thread-safe
class SortScriptWriter(private val probe: Probe) {
    val script: MutableList<ScriptLine> = ArrayList()
    private var focus: Int = 0

    fun focus(array: Array<Int>, index: Int) {
        //script.add(ScriptLine(array.copyOf(), index, probe.snapshot()))
        focus = index
    }

    fun replace(array: Array<Int>, index1: Int, index2: Int) {
        if (focus != index1) {
            throw RuntimeException("focus was lost: $focus != $index1")
        }

        //script.add(ScriptLine(array.copyOf(), index2, probe.snapshot()))
        focus = index2
    }

    class ScriptLine(
            val array: Array<Int>,
            val focus: Int,
            val probeSnapshot: Probe.Snapshot) {
    }
}