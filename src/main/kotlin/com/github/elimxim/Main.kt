package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.verify.CompareCommandVerifier
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.verify.VisualizeCommandVerifier
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.MainCommand
import com.github.elimxim.console.command.VisualizeCommand

fun main(args: Array<String>) {
    val jc = JCommander.newBuilder()
            .programName("cvsort (Sorting Algorithm Comparator & Visualizer)")
            .addObject(MainCommand)
            .addCommand("compare", CompareCommand)
            .addCommand("visualize", VisualizeCommand)
            .build()

    jc.parse(*args)

    if (MainCommand.bannerDisabled.not()) {
        ConsolePrinter.printBanner()
        ConsolePrinter.printEmptyLine()
    }

    if (MainCommand.usage) {
        jc.usage()
        return
    }

    if (MainCommand.listAlgorithms) {
        Algorithm.names().forEach { println(it) }
        return
    }

    if (jc.parsedCommand == "compare") {
        if (CompareCommandVerifier.verify(CompareCommand)) {
            processCompareCommand()
        }
    } else if (jc.parsedCommand == "visualize") {
        if (VisualizeCommandVerifier.verify(VisualizeCommand)) {
            processVisualizeCommand()
        }
    }
}

private fun processCompareCommand() {
    val algorithms = CompareCommand.algorithms.map {
        Algorithm.valueOf(it.uppercase())
    }

    val comparator = SortComparator(
            CompareCommand.arrayFile.toPath(),
            CompareCommand.printArray,
            CompareCommand.infoDisabled.not(),
            CompareCommand.arrayLength.toInt()
    )

    comparator.compare(algorithms)
}

private fun processVisualizeCommand() {
    val algorithm = Algorithm.valueOf(VisualizeCommand.algorithm.camelCaseToSnakeCase().uppercase())
    val sortSpeed = SortSpeed.valueOf(VisualizeCommand.speed.uppercase())
    val speedMillis = if (sortSpeed == SortSpeed.NONE) {
        VisualizeCommand.speedMillis.toLong()
    } else {
        sortSpeed.millis
    }

    val visualizer = SortVisualizer(
            speedMillis,
            VisualizeCommand.arrayLength.toInt(),
            VisualizeCommand.visualisationDisabled.not(),
            VisualizeCommand.pseudoCodeDisabled.not(),
            VisualizeCommand.infoDisabled.not()
    )

    visualizer.visualize(algorithm)
}