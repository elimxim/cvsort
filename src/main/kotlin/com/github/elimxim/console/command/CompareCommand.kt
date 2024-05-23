package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.console.parameter.*
import com.github.elimxim.withTimestamp

@Parameters(commandDescription = "compares two or more sorting algorithms")
object CompareCommand {
    @Parameter(
            description = "case-insensitive sorting algorithm names separated by a space",
            splitter = SpaceParameterSplitter::class,
            variableArity = true,
            required = true
    )
    var algorithms: List<String> = arrayListOf()

    @Parameter(
            names = ["--arraySize", "-s"],
            description = "array size: [2, 2^31)"
    )
    var arraySize: String = "40"

    @Parameter(
            names = ["--arrayFile", "-f"],
            description = "a file to save the array before sorting",
            converter = PathParameterConverter::class
    )
    var arrayFile: String = "array".withTimestamp() + ".txt"

    @Parameter(
            names = ["--printArray", "-p"],
            description = "prints the array to the specified file before sorting"
    )
    var printArray: Boolean = false

    @Parameter(
            names = ["--disableInfo", "--noInfo", "-ni"],
            description = "switches off the display of information about comparing algorithms"
    )
    var infoDisabled: Boolean = false
}

