package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

@Parameters(commandDescription = "visualizes the selected sorting algorithms")
object VisualizeCommand {
    @Parameter(
            description = "case-insensitive sorting algorithm name",
            required = true
    )
    lateinit var algorithm: String

    @Parameter(
            names = ["--disableVisualisation", "--noVisualisation", "-nv"],
            description = "disables visualisation"
    )
    var visualisationDisabled: Boolean = false

    @Parameter(
            names = ["--disablePseudoCode", "--noPseudoCode", "-nc"],
            description = "switches off display of pseudocode of the selected algorithm"
    )
    var pseudoCodeDisabled: Boolean = false

    @Parameter(
            names = ["--disableInfo", "--noInfo", "-ni"],
            description = "switches off the display of information about the comparison algorithm"
    )
    var infoDisabled: Boolean = false
}