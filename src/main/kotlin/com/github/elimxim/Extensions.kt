package com.github.elimxim

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.withTimestamp(literal: String = "_"): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
    return this + literal + formatter.format(LocalDateTime.now())
}

fun String.snakeCaseToCamelCase(): String {
    val chops = this.split("_")
    return if (chops.size > 1) {
        chops.joinToString(separator = "") { w ->
            w.lowercase().replaceFirstChar {
                it.titlecase()
            }
        }
    } else {
        this.replaceFirstChar {
            it.titlecase()
        }
    }
}

fun String.camelCaseToSnakeCase(upperCase: Boolean = true): String {
    val words = ArrayList<String>()
    val chops = this.split("_")
    if (chops.size > 1) {
        words.addAll(chops)
    } else {
        val stringBuilder = StringBuilder()
        this.toCharArray().forEach {
            if (it.isUpperCase()) {
                if (stringBuilder.isNotEmpty()) {
                    words.add(stringBuilder.toString())
                    stringBuilder.clear()
                }
                stringBuilder.append(it.lowercase())
            } else {
                stringBuilder.append(it)
            }
        }
        if (stringBuilder.isNotEmpty()) {
            words.add(stringBuilder.toString())
        }
    }

    return words.joinToString(separator = "_") {
        if (upperCase) it.uppercase() else it.lowercase()
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