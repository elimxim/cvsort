package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.elimxim.console.parameter.*
import com.github.elimxim.toPath
import com.github.elimxim.withTimestamp
import java.nio.file.Path

@Parameters(commandDescription = "compares two or more sorting algorithms")
object CompareCommand {
    @Parameter(
            description = "case-insensitive sorting algorithm names separated by a space",
            listConverter = AlgorithmParameterConverter::class,
            variableArity = true,
            required = true
    )
    var algorithms: List<String> = arrayListOf()

    @Parameter(
            names = ["--arraySize", "-s"],
            description = "array size: [2, 2^31)",
            validateWith = [ArraySizeParameterValidator::class]
    )
    var arraySize: Int = 40

    @Parameter(
            names = ["--arrayFile", "-f"],
            description = "a file to save the array before sorting",
            validateWith = [ArrayFileParameterValidator::class],
            converter = ArrayFileParameterConverter::class
    )
    var arrayFile: Path = ("array".withTimestamp() + ".txt").toPath()

    @Parameter(
            names = ["--printArray", "-p"],
            description = "prints the array to the specified file before sorting"
    )
    var printArray: Boolean = false

    @Parameter(
            names = ["--disableInfo", "--noInfo", "-ni"],
            description = "disables comparing algorithms info display"
    )
    var disableInfo: Boolean = false
}

