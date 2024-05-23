package com.github.elimxim.console.command

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

@Parameters
object MainCommand {
    @Parameter(
            names = ["--listAlgorithms", "--list", "-l"],
            description = "prints the available sorting algorithms"
    )
    var listAlgorithms: Boolean = false

    @Parameter(
            names = ["--disableBanner", "--noBanner", "-nb"],
            description = "disables banner display"
    )
    var bannerDisabled: Boolean = false

    @Parameter(
            names = ["--usage", "--help", "-h"],
            description = "shows usage",
            help = true,
            order = Int.MAX_VALUE
    )
    var usage: Boolean = false
}