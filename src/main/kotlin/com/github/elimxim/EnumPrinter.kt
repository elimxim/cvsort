package com.github.elimxim

import com.github.elimxim.console.Console
import com.github.elimxim.view.ComplexityView
import com.github.elimxim.view.SpeedGearView

class EnumPrinter(
        private val printSortNames: Boolean,
        private val printSortMethods: Boolean,
        private val printSpeedGears: Boolean,
        private val printComplexity: Boolean
) {
    fun print() {
        if (printSortNames) {
            Console.printLine("Sort names:")
            sortedEnumNames<SortName>().forEach { Console.printLine("- $it") }
            Console.printEmptyLine()
        }

        if (printSortMethods) {
            Console.printLine("Sort methods:")
            sortedEnumNames<SortMethod>().forEach { Console.printLine("- $it") }
            Console.printEmptyLine()
        }

        if (printSpeedGears) {
            Console.printLine("Speed gears:")
            Console.printLines(SpeedGearView().lines())
            Console.printEmptyLine()
        }

        if (printComplexity) {
            Console.printLine("Complexity:")
            Console.printLines(ComplexityView().lines())
            Console.printEmptyLine()
        }
    }
}