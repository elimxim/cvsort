package com.github.elimxim.console.parameter

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.ParameterException
import com.github.elimxim.Algorithm

class AlgorithmParameterConverter: IStringConverter<List<String>> {
    override fun convert(value: String): List<String> {
        val algorithms = value.split(" ").map { v ->
            if (!Algorithm.contains(v)) {
                throw ParameterException("unknown algorithm name: $v")
            }

            v.uppercase()
        }

        if (algorithms.size < 2) {
            throw ParameterException("the number of algorithms must be > 2")
        }

        if (algorithms.size > 10) {
            throw ParameterException("too many algorithms to compare")
        }

        return algorithms
    }
}