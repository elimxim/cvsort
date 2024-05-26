package com.github.elimxim

class ArrayScribingWrapper(
        private val delegate: ArrayWrapper<Int>,
        val scriptWriter: SortScriptWriterImpl
) : ArrayWrapper<Int> {
    override fun get(index: Int): Int {
        return delegate[index]
    }

    override fun set(index: Int, value: Int) {
        delegate[index] = value
    }

    override fun swap(index1: Int, index2: Int): Boolean {
        val result = delegate.swap(index1, index2)
        scriptWriter.replace(delegate, index1, index2)
        return result
    }

    override fun array(): Array<Int> {
        return delegate.array()
    }

    override fun size(): Int {
        return delegate.size()
    }
}