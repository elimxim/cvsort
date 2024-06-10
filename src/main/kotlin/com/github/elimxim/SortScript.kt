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
    fun line(extra: Extra)
    fun line(nothing: Nothing)
    fun ifEnabled(action: (SortScript) -> Unit)
    fun scene(): Scene
}

class Focus(val indexes: Set<Int> = emptySet()) {
    constructor(vararg indexes: Int) : this(indexes.toSet())
}

class Select(val indexes: Set<Int> = emptySet()) {
    constructor(vararg indexes: Int) : this(indexes.toSet())
}

class Swap(val index1: Int, val index2: Int)
class Move(val from: Int, val to: Int, val flash: Boolean = true)
class Extra(
        val array: IntArray,
        val focus: Focus = Focus(),
        val select: Select = Select()
) {
    constructor(vararg values: Int) : this(values.toList().toIntArray())
}

class Override(array: IntArray) {
    val array: IntArray

    init {
        this.array = array.copyOf()
    }
}

object Nothing

interface Scene : Queue<Frame>
class SceneImpl(lines: List<Frame>) : Scene, LinkedList<Frame>(lines)

// not thread safe
class Frame(val mainData: Data,
            val extraData: Data,
            val probeSnapshot: Probe.Snapshot
)

// not thread safe
class Data(val array: IntArray,
           val focused: Set<Int> = emptySet(),
           val selected: Set<Int> = emptySet()
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
    private val frames: MutableList<Frame> = ArrayList()

    override fun line(focus: Focus) {
        addFrame(focus = focus)
    }

    override fun line(focus: Focus, extra: Extra) {
        addFrame(focus = focus, extra = extra)
    }

    override fun line(focus: Focus, override: Override) {
        addFrame(focus = focus, override = override)
    }

    override fun line(select: Select) {
        addFrame(select = select)
    }

    override fun line(select: Select, extra: Extra) {
        addFrame(select = select, extra = extra)
    }

    override fun line(select: Select, override: Override) {
        addFrame(select = select, override = override)
    }

    override fun line(swap: Swap) {
        addFrame(select = Select(swap.index1, swap.index2))
    }

    override fun line(move: Move) {
        addFrame(move)
    }

    override fun line(move: Move, override: Override) {
        addFrame(move, override)
    }

    override fun line(bulkMove: BulkMove) {
        addFrame(bulkMove)
    }

    override fun line(bulkMove: BulkMove, extra: Extra) {
        addFrame(bulkMove, extra)
    }

    override fun line(extra: Extra) {
        frames.add(Frame(mainData = Data(
                array = arrayWrapper.original(),
        ), extraData = Data(
                array = extra.array,
                focused = extra.focus.indexes,
                selected = extra.select.indexes
        ), probeSnapshot = probe.snapshot()
        ))
    }

    override fun bulkMove(): BulkMove {
        return BulkMove(arrayWrapper.original())
    }

    override fun ifEnabled(action: (SortScript) -> Unit) {
        action(this)
    }

    override fun line(nothing: Nothing) {
        frames.add(Frame(
                mainData = Data(arrayWrapper.original()),
                extraData = Data(emptyArray<Int>().toIntArray()),
                probeSnapshot = probe.snapshot()
        ))
    }

    override fun scene(): Scene {
        return SceneImpl(frames)
    }

    private fun addFrame(focus: Focus? = null, select: Select? = null,
                         extra: Extra? = null, override: Override? = null) {
        frames.add(Frame(mainData = Data(
                array = override?.array ?: arrayWrapper.original(),
                focused = focus?.indexes ?: emptySet(),
                selected = select?.indexes ?: emptySet()
        ), extraData = Data(
                array = extra?.array ?: emptyArray<Int>().toIntArray(),
                focused = extra?.focus?.indexes ?: emptySet(),
                selected = extra?.select?.indexes ?: emptySet()
        ), probeSnapshot = probe.snapshot()
        ))
    }

    private fun addFrame(move: Move, override: Override? = null) {
        if (move.flash) {
            addFrame(focus = Focus(move.from), override = override)
            addFrame(select = Select(move.to), override = override)
        } else {
            frames.add(Frame(mainData = Data(
                    array = override?.array ?: arrayWrapper.original(),
                    focused = setOf(move.from),
                    selected = setOf(move.to)
            ), extraData = Data(
                    array = emptyArray<Int>().toIntArray()
            ), probeSnapshot = probe.snapshot()
            ))
        }
    }

    private fun addFrame(bulkMove: BulkMove, extra: Extra? = null) {
        if (bulkMove.isNotEmpty()) {
            val moves = bulkMove.moves()

            frames.add(Frame(mainData = Data(
                    array = bulkMove.original,
                    focused = moves.map { it.from }.toSet(),
            ), extraData = Data(
                    array = extra?.array ?: emptyArray<Int>().toIntArray(),
                    focused = extra?.focus?.indexes ?: emptySet(),
                    selected = extra?.select?.indexes ?: emptySet()
            ), probeSnapshot = probe.snapshot()
            ))

            frames.add(Frame(mainData = Data(
                    array = arrayWrapper.original(),
                    selected = moves.map { it.to }.toSet(),
            ), extraData = Data(
                    array = extra?.array ?: emptyArray<Int>().toIntArray(),
                    focused = extra?.focus?.indexes ?: emptySet(),
                    selected = extra?.select?.indexes ?: emptySet()
            ), probeSnapshot = probe.snapshot()
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

    override fun line(extra: Extra) {
    }

    override fun line(nothing: Nothing) {
    }

    override fun bulkMove(): BulkMove {
        return BulkMove(emptyArray<Int>().toIntArray())
    }

    override fun ifEnabled(action: (SortScript) -> Unit) {
    }

    override fun scene(): Scene {
        throw UnsupportedOperationException("no op")
    }
}