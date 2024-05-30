package com.github.elimxim.console.verify

import com.github.elimxim.Algorithm
import com.github.elimxim.SortSpeed
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.command.VisualizeCommand

object VisualizeCommandVerifier {
    fun verify(cmd: VisualizeCommand): Boolean {
        return checkAlgorithm(cmd.algorithm)
                && checkSpeed(cmd.speed)
                && checkSpeedMillis(cmd.speedMillis)
                && checkArrayLength(cmd.arrayLength)
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

    private fun checkArrayLength(arrayLength: String): Boolean {
        try {
            val number = arrayLength.toInt()
            if (number !in 2..Int.MAX_VALUE) {
                ConsolePrinter.printError("$arrayLength value must be in [10, 30]")
                return false
            }
            return true
        } catch (e: NumberFormatException) {
            ConsolePrinter.printError("$arrayLength value must be an integer number")
            return false
        }
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