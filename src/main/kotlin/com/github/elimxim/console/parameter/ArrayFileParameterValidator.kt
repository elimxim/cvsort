package com.github.elimxim.console.parameter

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException
import com.github.elimxim.toPath
import java.nio.file.Files

class ArrayFileParameterValidator: IParameterValidator {
    override fun validate(name: String?, value: String?) {
        if (value == null) {
            throw ParameterException("$name value is missing")
        }

        val path = value.toPath()

        if (Files.exists(path)) {
            throw ParameterException("$value already exists")
        }

        if (!Files.exists(path.parent)) {
            throw ParameterException("$value parent directory doesn't exist")
        }
    }
}