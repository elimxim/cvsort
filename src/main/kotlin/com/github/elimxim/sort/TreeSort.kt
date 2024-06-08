package com.github.elimxim.sort

import com.github.elimxim.*
import com.github.elimxim.Probe.Counter.*

@SortAlgorithm(
        timeComplexity = TimeComplexity(
                worst = Complexity.QUADRATIC,
                average = Complexity.LINEARITHMIC,
                best = Complexity.LINEARITHMIC
        ),
        spaceComplexity = Complexity.LINEAR,
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
            
        class Tree()
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
        val original = array.original()
        script.ifEnabled {
            for (i in 0..<array.size()) {
                it.line(Focus(i), Extra(original.take(i + 1)))
                array[i] = 0
            }
        }

        val tree = Tree(probe)
        for (i in original.indices) {
            probe.increment(ITERATIONS)
            tree.add(original[i])
            script.ifEnabled {
                traverseTree(tree.root, array, 0, false)
                val remaining = original.drop(i + 1).toIntArray() + IntArray(1)
                it.line(Extra(*remaining))
            }
        }

        traverseTree(tree.root, array, 0, true)
    }

    private fun traverseTree(node: Tree.Node?, array: IntArrayWrapper, index: Int, probeEnabled: Boolean): Int {
        if (probeEnabled) {
            probe.increment(ITERATIONS)
        }

        var newIndex = index
        if (node != null) {
            newIndex = traverseTree(node.left, array, newIndex, probeEnabled)
            array[newIndex++] = node.value
            newIndex = traverseTree(node.right, array, newIndex, probeEnabled)
        }
        return newIndex
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
                   var right: Node? = null
        )
    }
}