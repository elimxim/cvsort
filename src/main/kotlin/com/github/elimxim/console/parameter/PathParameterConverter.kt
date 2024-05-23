package com.github.elimxim.console.parameter

import com.beust.jcommander.IStringConverter
import com.github.elimxim.toPath
import java.nio.file.Path

class PathParameterConverter: IStringConverter<Path> {
    override fun convert(value: String): Path {
        return value.toPath()
    }
}