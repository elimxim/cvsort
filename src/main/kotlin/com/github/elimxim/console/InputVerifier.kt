package com.github.elimxim.console

import com.github.elimxim.SortName
import com.github.elimxim.SortSpeed
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.VisualizeCommand
import com.github.elimxim.toPath
import java.nio.file.Files
import java.nio.file.InvalidPathException

object InputVerifier {
    fun verify(cmd: CompareCommand): Boolean {
        return checkSortNames(cmd.sortNames)
                && checkArrayLength(cmd.arrayLength, min = 2, max = Int.MAX_VALUE, maxDisplay = "2^32-1")
                && checkArrayFile(cmd.arrayFile)
    }

    fun verify(cmd: VisualizeCommand): Boolean {
        return checkSortName(cmd.sortName)
                && checkSpeed(cmd.speed)
                && checkSpeedMillis(cmd.speedMillis)
                && checkArrayLength(cmd.arrayLength, min = 2, max = 30)
                && checkSwitchers(cmd.visualisationDisabled, cmd.pseudoCodeDisabled, cmd.infoDisabled)
    }

    private fun checkSortName(name: String): Boolean {
        val sortName = SortName.find(name, camelCase = true)
        if (sortName == null) {
            ConsolePrinter.printError("unknown sorting algorithm name: $name")
            val closeName = SortName.find(name, camelCase = true, ignoreCase = true)
            if (closeName != null) {
                ConsolePrinter.printLine("you might have meant: ${closeName.camelCase()}")
            }
            return false
        }
        return true
    }

    private fun checkSortNames(names: List<String>): Boolean {
        names.forEach {
            if (!checkSortName(it)) {
                return false
            }
        }

        if (names.size < 2) {
            ConsolePrinter.printError("the number of sorting algorithms cannot be less that 2")
            return false
        }

        if (names.size > 10) {
            ConsolePrinter.printError("the number of sorting algorithms cannot be more than 10")
            return false
        }

        return true
    }

    private fun checkArrayLength(length: String, min: Int, max: Int, minDisplay: String = "", maxDisplay: String = ""): Boolean {
        try {
            val number = length.toInt()
            if (number !in min..max) {
                val from = minDisplay.ifEmpty { min.toString() }
                val to = maxDisplay.ifEmpty { max.toString() }
                ConsolePrinter.printError("$length value must be in [$from, $to]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            ConsolePrinter.printError("$length value must be an integer number")
            return false
        }
    }

    private fun checkArrayFile(file: String): Boolean {
        try {
            val path = file.toPath()
            if (Files.exists(path)) {
                ConsolePrinter.printError("$file already exists")
                return false
            }

            if (!Files.exists(path.parent)) {
                ConsolePrinter.printError("$file parent directory doesn't exist")
                return false
            }

            return true
        } catch (e: InvalidPathException) {
            ConsolePrinter.printError(e.localizedMessage)
            return false
        }
    }

    private fun checkSpeed(speed: String): Boolean {
        if (!SortSpeed.contains(speed)) {
            ConsolePrinter.printError("unknown sort speed: $speed")
            return false
        }
        return true
    }

    private fun checkSpeedMillis(speedMillis: String): Boolean {
        try {
            if (speedMillis.toLong() !in 50..1000) {
                ConsolePrinter.printError("speedMillis must be in range [50..1000]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            ConsolePrinter.printError("$speedMillis value must be an integer number")
            return false
        }
    }

    private fun checkSwitchers(
            visualisationDisabled: Boolean,
            pseudoCodeDisabled: Boolean,
            infoDisabled: Boolean
    ): Boolean {
        if (visualisationDisabled && pseudoCodeDisabled && infoDisabled) {
            ConsolePrinter.printError("everything's off, you have to turn on at least one option")
            return false
        }
        return true
    }
}