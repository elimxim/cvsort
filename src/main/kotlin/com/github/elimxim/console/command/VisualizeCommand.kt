package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.SortSpeed

@Parameters(commandDescription = "visualizes the selected sorting algorithms")
object VisualizeCommand {
    const val NAME = "visualize"

    @Parameter(
            description = "<name>",
            required = true
    )
    lateinit var sortName: String

    @Parameter(
            names = ["--speed", "-s"],
            description = "sets the speed for the sorting visualisation"
    )
    var speed: String = SortSpeed.KOALA.name.lowercase()

    @Parameter(
            names = ["--speedMillis", "--millis", "-m"],
            description = "sets the speed in milliseconds for the sorting visualisation: [50..4000]",
            hidden = true
    )
    var speedMillis: String = "0"

    @Parameter(
            names = ["--arrayLength", "-l"],
            description = "array length: [10, 30]"
    )
    var arrayLength: String = "20"

    @Parameter(
            names = ["--disableInfo", "--noInfo", "-ni"],
            description = "switches off the display of information about the sorting algorithm"
    )
    var infoDisabled: Boolean = false
}