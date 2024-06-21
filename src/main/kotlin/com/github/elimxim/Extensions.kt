package com.github.elimxim

import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.withTimestamp(pattern: String, separator: String = "_"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this + separator + formatter.format(LocalDateTime.now())
}

fun String.snakeCaseToCamelCase(separator: String = ""): String {
    val chops = this.split("_")
    return if (chops.size > 1) {
        chops.joinToString(separator = separator) { w ->
            w.lowercase().replaceFirstChar {
                it.titlecase()
            }
        }
    } else {
        this.lowercase().replaceFirstChar {
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

fun IntRange.toIntArray(): IntArray {
    return this.toList().toIntArray()
}

fun Boolean.xnor(other: Boolean): Boolean {
    return this.xor(other).not()
}

fun Boolean.yesNo(): String {
    return if (this) "Yes" else "No"
}

class YesNoParseException(message: String): RuntimeException(message)

fun String.yesNo(ignoreCase: Boolean = false): Boolean {
    return if (this.equals("Yes", ignoreCase = ignoreCase)) {
        true
    } else if (this.equals("No", ignoreCase = ignoreCase)) {
        false
    } else {
        throw YesNoParseException("cannot recognize the string '$this' as 'Yes' or 'No'")
    }
}