package com.github.elimxim

import java.nio.file.Files
import java.nio.file.Path

class ArrayPrinter(private val path: Path) {
    fun printArray(array: IntArray) {
        Files.newBufferedWriter(path).use { out ->
            out.write("[")
            for (i in 0..<array.size - 1) {
                out.write("" + array[i])
                out.write(", ")
            }
            out.write(array.last())
            out.write("]")
        }
    }
}