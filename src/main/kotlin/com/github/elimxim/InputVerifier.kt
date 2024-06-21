package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.DiscoverCommand
import com.github.elimxim.console.command.InfoCommand
import com.github.elimxim.console.command.VisualizeCommand
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.time.LocalDate

class InputVerifier(private val args: Array<String>) {
    fun verify(cmd: CompareCommand): Boolean {
        return checkSortNames(cmd.sortNames, min = 2, max = 10, allAvailable = false, duplicationAvailable = true)
                && checkArrayLength(cmd.arrayLength, min = 2, max = Int.MAX_VALUE/2, maxDisplay = "2^30-1")
                && checkArrayFile(cmd.arrayFile)
    }

    fun verify(cmd: DiscoverCommand): Boolean {
        return checkEnumValues<Complexity>(cmd.worstTimeComplexity)
                && checkEnumValues<Complexity>(cmd.worstSpaceComplexity)
                && checkEnumValues<SortMethod>(cmd.methods)
                && checkYesNo(cmd.recursive)
                && checkYesNo(cmd.stable)
                && checkIntegerValues(cmd.inventionYears, min = 0, max = LocalDate.now().year)
    }

    fun verify(cmd: InfoCommand): Boolean {
        return checkSortNames(cmd.sortNames, min = 1, max = 20, allAvailable = true, duplicationAvailable = false)
    }

    fun verify(cmd: VisualizeCommand): Boolean {
        return checkEnumValue<SortName>(cmd.sortName)
                && checkEnumValue<SpeedGear>(cmd.speedGear)
                && checkIntegerValue(cmd.frameDelayMillis, min = 50, max = 4000)
                && checkArrayLength(cmd.arrayLength, min = 2, max = 40)
    }

    private inline fun <reified T : Enum<T>> checkEnumValues(values: List<String>): Boolean {
        values.forEach {
            if (checkEnumValue<T>(it).not()) {
                return false
            }
        }

        return true
    }

    private inline fun <reified T : Enum<T>> checkEnumValue(value: String): Boolean {
        if (notExistingEnum<T>(value, ignoreCase = true)) {
            if (notExistingEnum<T>(value, camelCase = true)) {
                printError("unknown value: $value")
                val closeValue = findEnum<T>(value, camelCase = true, ignoreCase = true)
                if (closeValue != null) {
                    Console.printLine("SUGGESTION: you might have meant: ${closeValue.camelCase()} or ${closeValue.name}")
                }
                return false
            }
        }
        return true
    }

    private fun checkSortNames(
            values: List<String>, min: Int, max: Int,
            allAvailable: Boolean, duplicationAvailable: Boolean
    ): Boolean {
        values.forEach {
            if (!checkEnumValue<SortName>(it)) {
                return false
            }
        }

        if (values.size < min) {
            printError("the number of sorting algorithms cannot be less that $min")
            return false
        }

        if (values.size > max) {
            printError("the number of sorting algorithms cannot be more than $max")
            return false
        }

        val allOptions = values.filter { it.equals(SortName.ALL.name, ignoreCase = true) }
        if (allOptions.isNotEmpty()) {
            if (allAvailable.not()) {
                printError("'ALL' option is not available")
                return false
            }
            if (values.size > 1) {
                printWarning("if 'ALL' is selected, all other sorting algorithm names will be ignored")
            }
        }

        if (duplicationAvailable.not()) {
            val duplicates = values.groupingBy { it.lowercase() }
                    .eachCount()
                    .filter { it.value > 1 }

            if (duplicates.isNotEmpty()) {
                printError("duplication of sorting algorithm names is not allowed")
                return false
            }
        }

        return true
    }

    private fun checkArrayLength(value: String, min: Int, max: Int, minDisplay: String = "", maxDisplay: String = ""): Boolean {
        try {
            val number = value.toInt()
            if (number !in min..max) {
                val from = minDisplay.ifEmpty { min.toString() }
                val to = maxDisplay.ifEmpty { max.toString() }
                printError("$value value must be in [$from, $to]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            printError("$value value must be an integer number")
            return false
        }
    }

    private fun checkArrayFile(value: String): Boolean {
        try {
            val path = value.toPath()
            if (Files.exists(path)) {
                printError("$value already exists")
                return false
            }

            if (!Files.exists(path.parent)) {
                printError("$value parent directory doesn't exist")
                return false
            }

            return true
        } catch (e: InvalidPathException) {
            printError(e.localizedMessage)
            return false
        }
    }

    private fun checkYesNo(value: String?): Boolean {
        if (value != null) {
            try {
                value.yesNo(ignoreCase = true)
            } catch (e: YesNoParseException) {
                printError(e.localizedMessage)
                return false
            }
        }

        return true
    }

    private fun checkIntegerValues(values: List<String>, min: Int, max: Int): Boolean {
        values.forEach {
            if (!checkIntegerValue(it, min, max)) {
                return false
            }
        }

        return true
    }

    private fun checkIntegerValue(value: String?, min: Int, max: Int): Boolean {
        try {
            if (value != null) {
                if (value.toLong() !in min..max) {
                    printError("'$value' must be in range [$min..$max]")
                    return false
                }
            }
            return true
        } catch (e: NumberFormatException) {
            printError("'$value' value must be an integer number")
            return false
        }
    }

    private fun printError(msg: String) {
        printInput()
        Console.printError(msg)
    }

    private fun printWarning(msg: String) {
        printInput()
        Console.printWarning(msg)
    }

    private fun printInput() {
        Console.printLine("INPUT: " + args.joinToString(separator = " "))
    }
}