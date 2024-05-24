package com.github.elimxim

class ArrayScribingWrapper(
        private val delegate: ArrayWrapper<Int>,
        val scriptWriter: SortScriptWriter
) : ArrayWrapper<Int> {
    override fun get(index: Int): Int {
        return delegate[index]
    }

    override fun set(index: Int, value: Int) {
        delegate[index] = value
    }

    override fun swap(index1: Int, index2: Int): Boolean {
        val result = delegate.swap(index1, index2)
        scriptWriter.replace(delegate.copy(), index1, index2)
        return result
    }

    // todo remove that
    override fun copy(): Array<Int> {
        return delegate.copy()
    }

    override fun size(): Int {
        return delegate.size()
    }
}