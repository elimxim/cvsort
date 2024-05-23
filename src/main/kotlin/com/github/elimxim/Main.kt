package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.InputVerifier
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.MainCommand
import com.github.elimxim.console.command.VisualizeCommand

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

    if (!MainCommand.bannerDisabled) {
        ConsolePrinter.printBanner()
        ConsolePrinter.printSpaceLine()
    }

    if (jc.parsedCommand == "compare") {
        InputVerifier.verifyCompareCommand(CompareCommand)
        processCompareCommand()
    } else if (jc.parsedCommand == "visualize") {
        InputVerifier.verifyVisualizeCommand(VisualizeCommand)
        processVisualizeCommand()
    }
}

private fun processCompareCommand() {
    val generator = TestArrayGenerator()
    val algorithms = CompareCommand.algorithms.map {
        Algorithm.valueOf(it.uppercase())
    }

    val comparator = SortComparator(
            CompareCommand.arrayFile.toPath(),
            CompareCommand.printArray,
            CompareCommand.infoDisabled.not()
    )

    val array = generator.generate(CompareCommand.arraySize.toInt())
    comparator.compare(algorithms, array)
}

private fun processVisualizeCommand() {

}