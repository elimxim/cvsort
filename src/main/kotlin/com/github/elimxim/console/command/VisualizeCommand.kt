package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.SpeedGear

@Parameters(commandDescription = "visualizes the selected sorting algorithms")
object VisualizeCommand {
    const val NAME = "visualize"

    @Parameter(
            description = "<name>",
            required = true
    )
    lateinit var sortName: String

    @Parameter(
            names = ["--speedGear", "--speed", "-s"],
            description = "sets the speed for the sorting visualisation"
    )
    var speedGear: String = SpeedGear.G4.name

    @Parameter(
            names = ["--frameDelayMillis", "--millis", "-m"],
            description = "sets the speed in milliseconds for the sorting visualisation: [50..4000]"
    )
    var frameDelayMillis: String? = null

    @Parameter(
            names = ["--arrayLength", "-l"],
            description = "array length: [2, 40]"
    )
    var arrayLength: String = "20"

    @Parameter(
            names = ["--skipShuffle", "-ss"],
            description = "switches off visualisation of array shuffling"
    )
    var shuffleSkipped: Boolean = false

    @Parameter(
            names = ["--disableInfo", "--noInfo", "-ni"],
            description = "switches off the display of information about the sorting algorithm"
    )
    var infoDisabled: Boolean = false
}