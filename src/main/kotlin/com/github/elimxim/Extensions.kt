package com.github.elimxim

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.withTimestamp(literal: String = "_"): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
    return this + literal + formatter.format(LocalDateTime.now())
}

fun String.snakeCase(): String {
    val words = this.split("_")
    return words.joinToString(separator = "") { w ->
        w.lowercase().replaceFirstChar {
            it.titlecase()
        }
    }
}

fun String.toPath(): Path {
    return if (this.startsWith("/")) {
        Path.of(this)
    } else {
        Path.of(System.getProperty("user.dir"), this)
    }
}

fun IntArray.swap(idx1: Int, idx2: Int) {
    val tmp = this[idx1]
    this[idx1] = this[idx2]
    this[idx2] = tmp
}