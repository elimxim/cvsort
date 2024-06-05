package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.ConsolePrinter
import com.github.elimxim.console.InputVerifier
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.InfoCommand
import com.github.elimxim.console.command.MainCommand
import com.github.elimxim.console.command.VisualizeCommand

fun main(args: Array<String>) {
    val jc = JCommander.newBuilder()
            .programName("cvsort")
            .addObject(MainCommand)
            .addCommand(CompareCommand.NAME, CompareCommand)
            .addCommand(VisualizeCommand.NAME, VisualizeCommand)
            .addCommand(InfoCommand.NAME, InfoCommand)
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

    if (MainCommand.listSortNames) {
        printSortNames()
        return
    }

    if (MainCommand.listSpeeds) {
        printSortSpeeds()
        return
    }

    val verifier = InputVerifier(args)
    if (jc.parsedCommand == CompareCommand.NAME) {
        if (verifier.verify(CompareCommand)) {
            processCompareCommand()
        }
    } else if (jc.parsedCommand == VisualizeCommand.NAME) {
        if (verifier.verify(VisualizeCommand)) {
            processVisualizeCommand()
        }
    } else if (jc.parsedCommand == InfoCommand.NAME) {
        if (verifier.verify(InfoCommand)) {
            processInfoCommand()
        }
    }
}

private fun processCompareCommand() {
    val sortNames = CompareCommand.sortNames.map {
        SortName.determine(it)
    }

    val comparator = SortComparator(
            CompareCommand.arrayLength.toInt(),
            CompareCommand.arrayFile.toPath(),
            CompareCommand.printArray,
            CompareCommand.infoDisabled.not()
    )

    comparator.compare(sortNames)
}

private fun processVisualizeCommand() {
    val sortName = SortName.determine(VisualizeCommand.sortName)
    val sortSpeed = SortSpeed.valueOf(VisualizeCommand.speed.uppercase())
    val speedMillis = VisualizeCommand.speedMillis.toLong()

    val visualizer = SortVisualizer(
            if (speedMillis != 0L) speedMillis else sortSpeed.millis,
            VisualizeCommand.arrayLength.toInt(),
            VisualizeCommand.infoDisabled.not()
    )

    visualizer.visualize(sortName)
}

private fun processInfoCommand() {
    var sortNames = InfoCommand.sortNames.map {
        SortName.determine(it)
    }

    if (sortNames.contains(SortName.ALL)) {
        sortNames = SortName.realValues().toList()
    }

    SortInfoShower().showInfo(sortNames)
}

private fun printSortNames() {
    SortName.names().forEach { ConsolePrinter.printLine("- $it") }
}

private fun printSortSpeeds() {
    SortSpeed.entries.sortedBy {
        it.millis
    }.forEach {
        ConsolePrinter.printLine("- ${it.name.lowercase()}: ${it.millis} millis delay")
    }
}