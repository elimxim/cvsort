package com.github.elimxim.console

import com.github.elimxim.SortName
import com.github.elimxim.SortSpeed
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.InfoCommand
import com.github.elimxim.console.command.VisualizeCommand
import com.github.elimxim.toPath
import java.nio.file.Files
import java.nio.file.InvalidPathException

class InputVerifier(private val args: Array<String>) {
    fun verify(cmd: CompareCommand): Boolean {
        return checkSortNames(cmd.sortNames, min = 2, max = 10, allAvailable = false, repetitionAvailable = true)
                && checkArrayLength(cmd.arrayLength, min = 2, max = Int.MAX_VALUE, maxDisplay = "2^32-1")
                && checkArrayFile(cmd.arrayFile)
    }

    fun verify(cmd: VisualizeCommand): Boolean {
        return checkSortName(cmd.sortName)
                && checkSpeed(cmd.speed)
                && checkSpeedMillis(cmd.speedMillis, min = 50, max = 1000)
                && checkArrayLength(cmd.arrayLength, min = 2, max = 30)
    }

    fun verify(cmd: InfoCommand): Boolean {
        return checkSortNames(cmd.sortNames, min = 1, max = 20, allAvailable = true, repetitionAvailable = false)
    }

    private fun checkSortName(name: String): Boolean {
        if (SortName.find(name, ignoreCase = true) == null) {
            if (SortName.find(name, camelCase = true) == null) {
                printError("unknown sorting algorithm name: $name")
                val closeName = SortName.find(name, camelCase = true, ignoreCase = true)
                if (closeName != null) {
                    ConsolePrinter.printLine("SUGGESTION: you might have meant: ${closeName.camelCase()} or ${closeName.name}")
                }
                return false
            }
        }
        return true
    }

    private fun checkSortNames(
            names: List<String>, min: Int, max: Int,
            allAvailable: Boolean, repetitionAvailable: Boolean
    ): Boolean {
        names.forEach {
            if (!checkSortName(it)) {
                return false
            }
        }

        if (names.size < min) {
            printError("the number of sorting algorithms cannot be less that $min")
            return false
        }

        if (names.size > max) {
            printError("the number of sorting algorithms cannot be more than $max")
            return false
        }

        val allOptions = names.filter { it.equals(SortName.ALL.name, ignoreCase = true) }
        if (allOptions.isNotEmpty()) {
            if (allAvailable.not()) {
                printError("'ALL' option is not available")
                return false
            }
            if (names.size > 1) {
                printWarning("if 'ALL' is selected, all other sorting algorithm names will be ignored")
            }
        }

        if (repetitionAvailable.not()) {
            val duplicates = names.groupingBy { it.lowercase() }
                    .eachCount()
                    .filter { it.value > 1 }

            if (duplicates.isNotEmpty()) {
                printError("duplication of sorting algorithm names is not allowed")
                return false
            }
        }

        return true
    }

    private fun checkArrayLength(length: String, min: Int, max: Int, minDisplay: String = "", maxDisplay: String = ""): Boolean {
        try {
            val number = length.toInt()
            if (number !in min..max) {
                val from = minDisplay.ifEmpty { min.toString() }
                val to = maxDisplay.ifEmpty { max.toString() }
                printError("$length value must be in [$from, $to]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            printError("$length value must be an integer number")
            return false
        }
    }

    private fun checkArrayFile(file: String): Boolean {
        try {
            val path = file.toPath()
            if (Files.exists(path)) {
                printError("$file already exists")
                return false
            }

            if (!Files.exists(path.parent)) {
                printError("$file parent directory doesn't exist")
                return false
            }

            return true
        } catch (e: InvalidPathException) {
            printError(e.localizedMessage)
            return false
        }
    }

    private fun checkSpeed(speed: String): Boolean {
        if (!SortSpeed.contains(speed)) {
            printError("unknown sort speed: $speed")
            return false
        }
        return true
    }

    private fun checkSpeedMillis(speedMillis: String, min: Int, max: Int): Boolean {
        try {
            if (speedMillis.toLong() !in min..max) {
                printError("speedMillis must be in range [$min..$max]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            printError("$speedMillis value must be an integer number")
            return false
        }
    }

    private fun printError(msg: String) {
        printInput()
        ConsolePrinter.printError(msg)
    }

    private fun printWarning(msg: String) {
        printInput()
        ConsolePrinter.printWarning(msg)
    }

    private fun printInput() {
        ConsolePrinter.printLine("INPUT: " + args.joinToString(separator = " "))
    }
}