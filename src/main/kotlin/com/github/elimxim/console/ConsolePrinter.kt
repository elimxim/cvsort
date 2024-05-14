package com.github.elimxim.console

object ConsolePrinter {
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

    fun printLines(lines: List<String>, refresh: Boolean = false) {
        if (refresh) {
            print(cursorUp(lines.size))
        }

        lines.forEach {
            println(it)
        }
    }

    fun printSpaceLine() {
        printLines(listOf(""))
    }

    private fun cursorUp(rows: Int): String {
        return 0x1b.toChar() + "[${rows}A\r"
    }
}