package com.github.elimxim.console

object Console {
    private val banner = """
          _____   _____  ___  ___ _____ 
         / __\ \ / / __|/ _ \| _ \_   _|
        | (__ \ V /\__ \ (_) |   / | |  
         \___| \_/ |___/\___/|_|_\ |_|  
    _________________________________________
    Sorting Algorithm Comparator & Visualizer
    """.trimIndent()

    fun printBanner() {
        printLines(banner.lines())
    }

    fun printLine(message: String) {
        println(message)
    }

    fun printLines(lines: List<String>, refresh: Boolean = false) {
        if (refresh) {
            print(cursorUp(lines.size))
        }

        lines.forEach {
            printLine(it)
        }
    }

    fun printError(message: String) {
        println("ERROR: $message")
    }

    fun printWarning(message: String) {
        println("WARNING: $message")
    }

    fun printEmptyLine() {
        println()
    }

    private fun cursorUp(lines: Int): String {
        return 0x1b.toChar() + "[${lines}A\r"
    }
}