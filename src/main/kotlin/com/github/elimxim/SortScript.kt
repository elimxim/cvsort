package com.github.elimxim

import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.util.LinkedList
import java.util.Queue

interface SortScript {
    fun action(vararg actions: Action)
    fun record(scene: (SortScript) -> Unit)
    fun record(): Record
}

interface Action
interface CoAction : Action
interface SingleAction : Action
interface ExtraAction : Action {
    fun array(): IntArray
    fun actions(): List<CoAction>
}

class Focus(val indexes: Set<Int> = emptySet()) : CoAction {
    constructor(vararg indexes: Int) : this(indexes.toSet())
}

class Select(val indexes: Set<Int> = emptySet()) : CoAction {
    constructor(vararg indexes: Int) : this(indexes.toSet())
}

class Swap(val index1: Int, val index2: Int) : SingleAction
class Move(val from: Int, val to: Int, val split: Boolean = true) : SingleAction

class BulkMove(array: IntArrayWrapper) : SingleAction {
    private val moves: MutableList<Move> = ArrayList()
    val arraySnap: IntArray

    init {
        arraySnap = array.snapshot()
    }

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

class Extra(array: IntArray, vararg actions: CoAction) : ExtraAction {
    private val actions: List<CoAction>
    private val arraySnap: IntArray

    init {
        this.actions = actions.toList()
        this.arraySnap = array.copyOf()
    }

    constructor(vararg values: Int) : this(values.toList().toIntArray())

    override fun array(): IntArray {
        return arraySnap
    }

    override fun actions(): List<CoAction> {
        return actions
    }
}

object Nothing : SingleAction

interface Record : Queue<Shot>

class RecordImpl(lines: List<Shot>) : Record, LinkedList<Shot>(lines)

class Shot(val mainScreen: Screen,
           val extraScreen: Screen,
           val probeSnapshot: Probe.Snapshot
)

class Screen(val array: IntArray,
             val focused: Set<Int> = emptySet(),
             val selected: Set<Int> = emptySet()
)

class SortScriptImpl(
        private val probe: Probe,
        private val arrayWrapper: IntArrayWrapper
) : SortScript {
    private val shots: MutableList<Shot> = ArrayList()

    override fun action(vararg actions: Action) {
        ActionVerifier.verify(actions.toList())
        shots.addAll(processActions(actions.toList()))
    }

    override fun record(scene: (SortScript) -> Unit) {
        scene(this)
    }

    override fun record(): Record {
        return RecordImpl(shots)
    }

    private fun processActions(actions: List<Action>): List<Shot> {
        val mainScreens = mutableListOf<Screen>()
        if (actions.any { it is SingleAction }) {
            mainScreens.addAll(processSingleAction(actions.find { it is SingleAction } as SingleAction))
        } else if (actions.any { it is CoAction }) {
            mainScreens.add(processCoActions(actions.filterIsInstance<CoAction>()))
        }

        val extraScreen = if (actions.any { it is ExtraAction }) {
            if (mainScreens.isEmpty()) {
                mainScreens.add(Screen(arrayWrapper.snapshot()))
            }
            processExtraAction(actions.find { it is ExtraAction } as ExtraAction)
        } else {
            Screen(emptyArray<Int>().toIntArray())
        }

        val probeSnapshot = probe.snapshot()

        return mainScreens.map {
            Shot(mainScreen = it, extraScreen = extraScreen, probeSnapshot)
        }
    }

    private fun processCoActions(actions: List<CoAction>): Screen {
        val focused = mutableSetOf<Int>()
        val selected = mutableSetOf<Int>()

        actions.forEach {
            when (it) {
                is Focus -> focused.addAll(it.indexes.toSet())
                is Select -> selected.addAll(it.indexes.toSet())
            }
        }

        return Screen(
                arrayWrapper.snapshot(),
                focused = focused.toSet(),
                selected = selected.toSet()
        )
    }

    private fun processSingleAction(action: SingleAction): List<Screen> {
        val arraySnap = arrayWrapper.snapshot()
        val screens = mutableListOf<Screen>()
        when (action) {
            is Swap -> {
                screens.add(Screen(arraySnap, selected = setOf(action.index1, action.index2)))
            }
            is Move -> {
                if (action.split) {
                    screens.add(Screen(arraySnap,
                            focused = setOf(action.from)))
                    screens.add(Screen(arraySnap,
                            selected = setOf(action.to)))
                } else {
                    screens.add(Screen(arraySnap,
                            focused = setOf(action.from),
                            selected = setOf(action.to)))
                }
            }
            is BulkMove -> {
                val moves = action.moves()
                screens.add(Screen(action.arraySnap,
                        focused = moves.map { it.from }.toSet()))
                screens.add(Screen(arraySnap,
                        selected = moves.map { it.to }.toSet()))
            }
            is Nothing -> {
                screens.add(Screen(arraySnap))
            }
        }
        return screens
    }

    private fun processExtraAction(action: ExtraAction): Screen {
        val focused = mutableSetOf<Int>()
        val selected = mutableSetOf<Int>()

        action.actions().forEach {
            when (it) {
                is Focus -> focused.addAll(it.indexes.toSet())
                is Select -> selected.addAll(it.indexes.toSet())
            }
        }

        return Screen(
                action.array(),
                focused = focused.toSet(),
                selected = selected.toSet()
        )
    }
}

class InvalidActionException(message: String) : RuntimeException(message)

object ActionVerifier {
    private val onlyOneSingleAction: (List<Action>) -> Unit = { actions ->
        if (actions.count { it is SingleAction } > 1) {
            throw InvalidActionException("found multiple single actions")
        }
    }

    private val noBothCoAndSingleActions: (List<Action>) -> Unit = { actions ->
        if (actions.any { it is CoAction } && actions.any { it is SingleAction }) {
            throw InvalidActionException("found both single action and co action")
        }
    }

    private val onlyOneExtraAction: (List<Action>) -> Unit = { actions ->
        if (actions.count { it is ExtraAction } > 1) {
            throw InvalidActionException("found multiple extra actions")
        }
    }

    fun verify(actions: List<Action>) {
        onlyOneSingleAction(actions)
        noBothCoAndSingleActions(actions)
        onlyOneExtraAction(actions)
    }
}

class NoOpSortScript : SortScript {
    override fun action(vararg actions: Action) {
    }

    override fun record(scene: (SortScript) -> Unit) {
    }

    override fun record(): Record {
        throw UnsupportedOperationException("no op")
    }
}