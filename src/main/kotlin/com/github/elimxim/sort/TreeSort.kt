package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*
import kotlin.math.round

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.LINEARITHMIC,
                best = Complexity.LINEARITHMIC
        ),
        spaceComplexity = Complexity.LINEAR,
        methods = [Method.INSERTION],
        stable = true,
        pseudoCode = """
        tree = invoke Tree()
        for i in [0..n] do
            invoke tree.add(array[i])    
        end
        
        invoke traverse(tree.root, array, 0)
        
        fun traverse(n, array, idx)
            if n is not nil then
                invoke traverse(array, n.left, idx)
                array[idx++] = n.v
                invoke traverse(array, n.right, idx)
            end
        end
            
        class Tree
            class Node(v, left, right)
            
            root: Node
            
            fun add(v)
                root = add(root, v)
            end
            
            fun add(n, v) -> n
                if n is nil then
                    -> invoke Node(v)
                end
                
                if v < n.v then
                    n.left = invoke add(n.left, v)
                else if key > n.v then
                    n.right = invoke add(n.right, v)
                end
            end            
        end
        """
)
class TreeSort(
        private val probe: Probe,
        private val script: SortScript
) : Sort {
    override fun sort(array: IntArrayWrapper) {
        val tree = Tree(probe)
        for (i in 0..<array.size()) {
            probe.increment(ITERATIONS)
            tree.add(array[i])
            script.scene {
                with(TraversalContext(tree.root, count = false, i + 1)) {
                    traverseTree(tree.root, this)
                    script.action(Focus(i), Extra(traversalArray, Focus(focused), Select(selected)))
                }
            }
        }

        with(TraversalContext(tree.root, count = true, array.size())) {
            traverseTree(tree.root, this)
            for (i in traversalArray.indices) {
                array[i] = traversalArray[i]
                script.action(Focus(i), Extra(traversalArray, Focus(focused), Select(selected)))
            }
        }
    }

    private class Tree(private val probe: Probe) {
        var root: Node? = null

        fun add(value: Int) {
            root = add(root, value)
        }

        private fun add(node: Node?, value: Int): Node {
            probe.increment(ITERATIONS)
            if (node == null) {
                return Node(value)
            }

            probe.increment(COMPARISONS)
            if (value < node.value) {
                node.left = add(node.left, value)
            }

            probe.increment(COMPARISONS)
            if (value > node.value) {
                node.right = add(node.right, value)
            }

            return node
        }

        class Node(val value: Int,
                   var left: Node? = null,
                   var right: Node? = null) {
            fun isLeaf(): Boolean {
                return left == null && right == null
            }
        }
    }

    private fun traverseTree(node: Tree.Node?, ctx: TraversalContext) {
        if (ctx.count) {
            probe.increment(ITERATIONS)
        }

        if (node != null) {
            traverseTree(node.left, ctx)

            ctx.traversalArray[ctx.index] = node.value
            script.scene {
                if (node.value == ctx.root?.value) {
                    ctx.selected.add(ctx.index)
                } else if (node.isLeaf().not()) {
                    ctx.focused.add(ctx.index)
                }
            }
            ctx.index++

            traverseTree(node.right, ctx)
        }
    }

    private class TraversalContext(
            val root: Tree.Node?, val count: Boolean, arraySize: Int) {
        val selected: MutableSet<Int> = HashSet()
        val focused: MutableSet<Int> = HashSet()
        val traversalArray: IntArray = IntArray(arraySize)
        var index: Int = 0
    }
}