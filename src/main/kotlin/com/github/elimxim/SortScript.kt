package com.github.elimxim

import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun line(focus: Focus)
    fun line(focus: Focus, extra: Extra)
    fun line(focus: Focus, override: Override)
    fun line(select: Select)
    fun line(select: Select, extra: Extra)
    fun line(select: Select, override: Override)
    fun line(swap: Swap)
    fun line(move: Move)
    fun line(move: Move, override: Override)
    fun line(bulkMove: BulkMove)
    fun line(bulkMove: BulkMove, extra: Extra)
    fun bulkMove(): BulkMove
    fun line(nothing: Nothing)
    fun screenplay(): Screenplay
}

class Focus(vararg val indexes: Int)
class Select(vararg val indexes: Int)
class Swap(val index1: Int, val index2: Int)
class Move(val from: Int, val to: Int, val flash: Boolean = true)
class Extra(val variable: Int)
class Override(array: IntArray) {
    val array: IntArray

    init {
        this.array = array.copyOf()
    }
}
class Nothing

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
class BulkMove(val original: IntArray) {
    private val moves: MutableList<Move> = ArrayList()

    fun add(from: Int, to: Int) {
        moves.add(Move(from, to))
    }

    fun isNotEmpty(): Boolean {
        return moves.isNotEmpty()
    }

    fun moves(): List<Move> {
        return moves.toList()
    }
}

// not thread safe
class SortScriptImpl(
        private val probe: Probe,
        private val arrayWrapper: IntArrayWrapper
) : SortScript {
    private val scriptLines: MutableList<ScriptLine> = ArrayList()

    override fun line(focus: Focus) {
        doFocus(focus)
    }

    override fun line(focus: Focus, extra: Extra) {
        doFocus(focus, extra = extra)
    }

    override fun line(focus: Focus, override: Override) {
        doFocus(focus, override = override)
    }

    override fun line(select: Select) {
        doSelect(select)
    }

    override fun line(select: Select, extra: Extra) {
        doSelect(select, extra = extra)
    }

    override fun line(select: Select, override: Override) {
        doSelect(select, override = override)
    }

    override fun line(swap: Swap) {
        doSelect(Select(swap.index1, swap.index2))
    }

    override fun line(move: Move) {
        doMove(move)
    }

    override fun line(move: Move, override: Override) {
        doMove(move, override)
    }

    override fun line(bulkMove: BulkMove) {
        doBulkMove(bulkMove)
    }

    override fun line(bulkMove: BulkMove, extra: Extra) {
        doBulkMove(bulkMove, extra)
    }

    override fun bulkMove(): BulkMove {
        return BulkMove(arrayWrapper.original())
    }

    override fun line(nothing: Nothing) {
        scriptLines.add(ScriptLine(
                array = arrayWrapper.original(),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun screenplay(): Screenplay {
        return ScreenplayImpl(scriptLines)
    }

    private fun doFocus(focus: Focus, extra: Extra? = null, override: Override? = null) {
        scriptLines.add(ScriptLine(
                array = override?.array ?: arrayWrapper.original(),
                variable = extra?.variable,
                focused = focus.indexes.toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    private fun doSelect(select: Select, extra: Extra? = null, override: Override? = null) {
        scriptLines.add(ScriptLine(
                array = override?.array ?: arrayWrapper.original(),
                variable = extra?.variable,
                selected = select.indexes.toSet(),
                probeSnapshot = probe.snapshot()
        ))
    }

    private fun doMove(move: Move, override: Override? = null) {
        if (move.flash) {
            doFocus(Focus(move.from), override = override)
            doSelect(Select(move.to), override = override)
        } else {
            scriptLines.add(ScriptLine(
                    array = override?.array ?: arrayWrapper.original(),
                    focused = setOf(move.from),
                    selected = setOf(move.to),
                    probeSnapshot = probe.snapshot()
            ))
        }
    }

    private fun doBulkMove(bulkMove: BulkMove, extra: Extra? = null) {
        if (bulkMove.isNotEmpty()) {
            val moves = bulkMove.moves()

            scriptLines.add(ScriptLine(
                    array = bulkMove.original,
                    variable = extra?.variable,
                    focused = moves.map { it.from }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))

            scriptLines.add(ScriptLine(
                    array = arrayWrapper.original(),
                    variable = extra?.variable,
                    selected =  moves.map { it.to }.toSet(),
                    probeSnapshot = probe.snapshot()
            ))
        }
    }
}

class NoOpSortScript : SortScript {
    override fun line(focus: Focus) {
    }

    override fun line(focus: Focus, extra: Extra) {
    }

    override fun line(focus: Focus, override: Override) {
    }

    override fun line(select: Select) {
    }

    override fun line(select: Select, extra: Extra) {
    }

    override fun line(select: Select, override: Override) {
    }

    override fun line(swap: Swap) {
    }

    override fun line(move: Move) {
    }

    override fun line(move: Move, override: Override) {
    }

    override fun line(bulkMove: BulkMove) {
    }

    override fun line(bulkMove: BulkMove, extra: Extra) {
    }

    override fun line(nothing: Nothing) {
    }

    override fun bulkMove(): BulkMove {
        return BulkMove(IntArray(0))
    }

    override fun screenplay(): Screenplay {
        throw UnsupportedOperationException("no op")
    }
}