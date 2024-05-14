package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.MainCommand

fun main(args: Array<String>) {
    val jc = JCommander.newBuilder()
            .programName("cvsort (Sorting Algorithm Comparator & Visualizer)")
            .addObject(MainCommand)
            .addCommand("compare", CompareCommand)
            .build()

    jc.parse(*args)

    if (MainCommand.usage) {
        jc.usage()
        return
    }

    if (MainCommand.listAlgorithms) {
        Algorithm.names().forEach { println(it) }
        return
    }

    if (!MainCommand.disableBanner) {
        ConsolePrinter.printBanner()
        ConsolePrinter.printSpaceLine()
    }

    if (jc.parsedCommand == "compare") {
        compareCommand()
    }
}

private fun compareCommand() {
    val array = TestArrayGenerator.generate(CompareCommand.arraySize)
    val algorithms = CompareCommand.algorithms.map {
        Algorithm.valueOf(it.uppercase())
    }

    val comparator = SortComparator(
            CompareCommand.arrayFile,
            CompareCommand.printArray,
            CompareCommand.disableInfo.not()
    )

    comparator.compare(algorithms, array)
}