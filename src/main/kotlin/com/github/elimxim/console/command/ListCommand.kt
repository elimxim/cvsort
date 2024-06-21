package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

@Parameters(commandDescription = "helps to understand other commands by providing enum lists")
object ListCommand {
    const val NAME = "list"

    @Parameter(
            names = ["--sortNames", "--names", "-n"],
            description = "prints the available sorting algorithms"
    )
    var sortNamesEnabled: Boolean = false

    @Parameter(
            names = ["--sortMethods", "--methods", "-m"],
            description = "prints the methods of sorting algorithms"
    )
    var sortMethodsEnabled: Boolean = false

    @Parameter(
            names = ["--speedGears", "--speeds", "-s"],
            description = "prints the available visualisation speeds of sorting algorithms"
    )
    var speedGearsEnabled: Boolean = false

    @Parameter(
            names = ["--complexity", "-c"],
            description = "prints the complexity of sorting algorithms"
    )
    var complexityEnabled: Boolean = false

    @Parameter(
            names = ["--usage", "--help", "-h"],
            description = "shows usage",
            help = true,
            order = Int.MAX_VALUE
    )
    var usage: Boolean = false
}