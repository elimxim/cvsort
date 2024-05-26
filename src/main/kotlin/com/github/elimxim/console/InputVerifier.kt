package com.github.elimxim.console

import com.github.elimxim.Algorithm
import com.github.elimxim.SortSpeed
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.VisualizeCommand
import com.github.elimxim.toPath
import java.nio.file.Files
import java.nio.file.InvalidPathException

object InputVerifier {
    fun verifyCompareCommand(cmd: CompareCommand): Boolean {
        return checkAlgorithms(cmd.algorithms)
                && checkArrayLength(cmd.arrayLength)
                && checkArrayFile(cmd.arrayFile)
    }

    private fun checkAlgorithms(algorithms: List<String>): Boolean {
        algorithms.forEach {
            if (!checkAlgorithm(it)) {
                return false
            }
        }

        if (algorithms.size < 2) {
            ConsolePrinter.printError("the number of algorithms cannot be less that 2")
            return false
        }

        if (algorithms.size > 10) {
            ConsolePrinter.printError("the number of algorithms cannot be more than 10")
            return false
        }

        return true
    }

    private fun checkArrayLength(size: String): Boolean {
        try {
            val number = size.toInt()
            if (number !in 2..Int.MAX_VALUE) {
                ConsolePrinter.printError("$size value must be in [2, 2^31)")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            ConsolePrinter.printError("$size value must be an integer number")
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

    fun verifyVisualizeCommand(cmd: VisualizeCommand): Boolean {
        return checkAlgorithm(cmd.algorithm)
                && checkSpeed(cmd.speed)
                && checkSwitchers(cmd.visualisationDisabled, cmd.pseudoCodeDisabled, cmd.infoDisabled)
    }

    private fun checkAlgorithm(algorithm: String): Boolean {
        if (!Algorithm.contains(algorithm)) {
            ConsolePrinter.printError("unknown algorithm name: $algorithm")
            return false
        }
        return true
    }

    private fun checkSpeed(speed: String): Boolean {
        if (!SortSpeed.contains(speed)) {
            ConsolePrinter.printError("unknown sort speed: $speed")
            return false
        }
        return true
    }

    private fun checkSwitchers(visualisationDisabled: Boolean,
                               pseudoCodeDisabled: Boolean,
                               infoDisabled: Boolean): Boolean {
        if (visualisationDisabled && pseudoCodeDisabled && infoDisabled) {
            ConsolePrinter.printError("everything's off, you have to turn on at least one option")
            return false
        }
        return true
    }
}