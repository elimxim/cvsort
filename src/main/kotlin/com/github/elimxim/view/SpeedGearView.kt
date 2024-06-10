package com.github.elimxim.view

import com.github.elimxim.SpeedGear
import de.vandermeer.asciitable.AsciiTable

class SpeedGearView : View {
    override fun lines(): List<String> {
        val table = AsciiTable()

        table.addRule()
        table.addRow(listOf(GEAR, MILLIS))
        table.addRule()

        SpeedGear.entries.sortedByDescending {
            it.frameDelayMillis
        }.forEach {
            table.addRow(listOf(it.name, it.frameDelayMillis))
        }

        table.addRule()
        return table.renderAsCollection().toList()
    }

    private companion object Header {
        const val GEAR = "Gear"
        const val MILLIS = "Frame delay millis"
    }
}