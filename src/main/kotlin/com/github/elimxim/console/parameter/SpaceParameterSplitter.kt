package com.github.elimxim.console.parameter

import com.beust.jcommander.converters.IParameterSplitter

class SpaceParameterSplitter: IParameterSplitter {
    override fun split(value: String?): MutableList<String> {
        return value!!.split(" ").toMutableList()
    }
}