package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.Console
import com.github.elimxim.console.InputVerifier
import com.github.elimxim.console.command.CompareCommand
import com.github.elimxim.console.command.InfoCommand
import com.github.elimxim.console.command.MainCommand
import com.github.elimxim.console.command.VisualizeCommand
import com.github.elimxim.view.SpeedGearView

fun main(args: Array<String>) {
    val jc = JCommander.newBuilder()
            .programName("cvsort")
            .addObject(MainCommand)
            .addCommand(InfoCommand.NAME, InfoCommand)
            .addCommand(CompareCommand.NAME, CompareCommand)
            .addCommand(VisualizeCommand.NAME, VisualizeCommand)
            .build()

    jc.parse(*args)

    if (MainCommand.bannerDisabled.not()) {
        Console.printBanner()
        Console.printEmptyLine()
    }

    if (MainCommand.usage) {
        jc.usage()
        return
    }

    if (MainCommand.listSortNames) {
        SortName.names().forEach { Console.printLine("- $it") }
        return
    }

    if (MainCommand.listSpeedGears) {
        Console.printLines(SpeedGearView().lines())
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
    val speedGear = SpeedGear.valueOf(VisualizeCommand.speedGear.uppercase())

    val visualizer = SortVisualizer(
            VisualizeCommand.frameDelayMillis?.toLong() ?: speedGear.frameDelayMillis,
            VisualizeCommand.arrayLength.toInt(),
            VisualizeCommand.shuffleSkipped.not(),
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
