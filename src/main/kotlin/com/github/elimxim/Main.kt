package com.github.elimxim

import com.beust.jcommander.JCommander
import com.github.elimxim.console.Console
import com.github.elimxim.console.command.*

fun main(args: Array<String>) {
    val jc = JCommander.newBuilder()
            .programName("cvsort")
            .addObject(MainCommand)
            .addCommand(CompareCommand.NAME, CompareCommand)
            .addCommand(DiscoverCommand.NAME, DiscoverCommand)
            .addCommand(InfoCommand.NAME, InfoCommand)
            .addCommand(ListCommand.NAME, ListCommand)
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
    } else if (CompareCommand.usage) {
        jc.usage(CompareCommand.NAME)
        return
    } else if (DiscoverCommand.usage) {
        jc.usage(DiscoverCommand.NAME)
        return
    } else if (InfoCommand.usage) {
        jc.usage(InfoCommand.NAME)
        return
    } else if (ListCommand.usage) {
        jc.usage(ListCommand.NAME)
        return
    } else if (VisualizeCommand.usage) {
        jc.usage(VisualizeCommand.NAME)
        return
    }

    val verifier = InputVerifier(args)
    when (jc.parsedCommand) {
        CompareCommand.NAME ->
            if (verifier.verify(CompareCommand)) {
                processCompareCommand(CompareCommand)
            }

        DiscoverCommand.NAME ->
            if (verifier.verify(DiscoverCommand)) {
                processDiscoverCommand(DiscoverCommand)
            }

        InfoCommand.NAME ->
            if (verifier.verify(InfoCommand)) {
                processInfoCommand(InfoCommand)
            }

        ListCommand.NAME ->
            processListCommand(ListCommand)

        VisualizeCommand.NAME ->
            if (verifier.verify(VisualizeCommand)) {
                processVisualizeCommand(VisualizeCommand)
            }
    }
}

private fun processCompareCommand(cmd: CompareCommand) {
    val sortNames = cmd.sortNames.map {
        getEnumValue<SortName>(it)
    }

    val comparator = SortComparator(
            arrayLength = cmd.arrayLength.toInt(),
            arrayFile = cmd.arrayFile.toPath(),
            printArray = cmd.printArray,
            showInfo = cmd.infoDisabled.not()
    )

    comparator.compare(sortNames)
}

private fun processDiscoverCommand(cmd: DiscoverCommand) {
    val attrs = SearchAttributes(
            nameTexts = cmd.nameTexts,
            worstTimeComplexity = cmd.worstTimeComplexity.map { getEnumValue<Complexity>(it) },
            worstSpaceComplexity = cmd.worstSpaceComplexity.map { getEnumValue<Complexity>(it) },
            methods = cmd.methods.map { getEnumValue<SortMethod>(it) },
            recursive = cmd.recursive,
            stable = cmd.stable,
            inventionYears = cmd.inventionYears.map { it.toInt() },
            authorTexts = cmd.authorTexts
    )

    val sortNames = SortSearcher.search(attrs)
    SortInfoPrinter(
            ignoreMissedAnno = false,
            withExtraInfo = true
    ).print(sortNames)
}

private fun processInfoCommand(cmd: InfoCommand) {
    val sortNames = cmd.sortNames.map {
        getEnumValue<SortName>(it)
    }

    SortInfoShower(cmd.extraForced).showInfo(sortNames)
}

private fun processListCommand(cmd: ListCommand) {
    val printer = EnumPrinter(
            printSortNames = cmd.sortNamesEnabled,
            printSortMethods = cmd.sortMethodsEnabled,
            printSpeedGears = cmd.speedGearsEnabled,
            printComplexity = cmd.complexityEnabled
    )

    printer.print()
}

private fun processVisualizeCommand(cmd: VisualizeCommand) {
    val sortName = getEnumValue<SortName>(cmd.sortName)
    val speedGear = SpeedGear.valueOf(cmd.speedGear.uppercase())

    val visualizer = SortVisualizer(
            frameDelayMillis = cmd.frameDelayMillis?.toLong() ?: speedGear.frameDelayMillis,
            arrayLength = cmd.arrayLength.toInt(),
            showShuffle = cmd.shuffleSkipped.not(),
            showInfo = cmd.infoDisabled.not(),
            reverse = speedGear == SpeedGear.R,
            casualMode = cmd.casualModeEnabled
    )

    visualizer.visualize(sortName)
}
