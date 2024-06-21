package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.console.parameter.SpaceParameterSplitter

@Parameters(commandDescription = "shows information about one or more sorting algorithms")
object InfoCommand {
    const val NAME = "info"

    @Parameter(
            description = "<name> [, <name-2>, ..., <name-20>]",
            splitter = SpaceParameterSplitter::class,
            variableArity = true,
            required = true
    )
    var sortNames: List<String> = arrayListOf()

    @Parameter(
            names = ["--forceExtra", "-e"],
            description = "always includes extra information into the result, such as the year of invention and the authors"
    )
    var extraForced: Boolean = false

    @Parameter(
            names = ["--usage", "--help", "-h"],
            description = "shows usage",
            help = true,
            order = Int.MAX_VALUE
    )
    var usage: Boolean = false
}