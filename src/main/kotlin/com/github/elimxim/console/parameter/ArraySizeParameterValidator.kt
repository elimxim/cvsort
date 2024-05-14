package com.github.elimxim.console.parameter

import com.beust.jcommander.IParameterValidator
import com.beust.jcommander.ParameterException

class ArraySizeParameterValidator : IParameterValidator {
    override fun validate(name: String?, value: String?) {
        if (value == null) {
            throw ParameterException("$name value is missing")
        }

        try {
            val number = value.toInt()
            if (number !in 2..Int.MAX_VALUE) {
                throw ParameterException("$name value must be in [2, 2^31)")
            }
        } catch (e: NumberFormatException) {
            throw ParameterException("$name value must be an integer number")
        }

    }
}