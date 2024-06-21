package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.console.parameter.SpaceParameterSplitter

@Parameters(commandDescription = "provides search attributes for discovering sorting algorithms; " +
        "if more than one parameter is selected, the search will operate as a logical AND")
object DiscoverCommand {
    const val NAME = "discover"

    @Parameter(
            names = ["--names", "--name", "-n"],
            description = "space-separated one or more texts for full-text search by name; " +
                    "if more than one value is recorded, the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true
    )
    var nameTexts: List<String> = emptyList()

    @Parameter(
            names = ["--timeComplexity", "--time", "-t"],
            description = "space-separated one or more worst time complexity; " +
                    "if more than one value is recorded, the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true
    )
    var worstTimeComplexity: List<String> = emptyList()

    @Parameter(
            names = ["--spaceComplexity", "--space", "-s"],
            description = "space-separated one or more worst space complexity; " +
                    "if more than one value is recorded, the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true
    )
    var worstSpaceComplexity: List<String> = emptyList()

    @Parameter(
            names = ["--methods", "--method", "-m"],
            description = "space-separated one or more sorting methods; " +
                    "if more than one value is recorded, the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true
    )
    var methods: List<String> = emptyList()

    @Parameter(
            names = ["--recursive", "-r"],
            description = "yes/no option to search for sorting algorithms with recursive implementation"
    )
    var recursive: String? = null

    @Parameter(
            names = ["--stable", "-b"],
            description = "yes/no option to search for stable sorting algorithms"
    )
    var stable: String? = null

    @Parameter(
            names = ["--years", "--year", "-y"],
            description = "space-separated one or more years of invention; " +
                    "if one year is recorded, the search will interpret it as an inclusive range: [y, current]; " +
                    "if two years are recorded, the search will interpret it as an inclusive range: [y1, y2]; " +
                    "otherwise the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true,
    )
    var inventionYears: List<String> = emptyList()

    @Parameter(
            names = ["--authors", "--author", "-a"],
            description = "space-separated one or more author texts for full-text search by author; " +
                    "if more than one value is recorded, the search will operate as a logical OR",
            splitter = SpaceParameterSplitter::class,
            variableArity = true
    )
    var authorTexts: List<String> = emptyList()

    @Parameter(
            names = ["--usage", "--help", "-h"],
            description = "shows usage",
            help = true,
            order = Int.MAX_VALUE
    )
    var usage: Boolean = false
}