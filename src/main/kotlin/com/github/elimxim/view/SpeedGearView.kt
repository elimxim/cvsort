package com.github.elimxim.view

import com.github.elimxim.SpeedGear
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine

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

        table.setPaddingLeftRight(1, 1)
        table.renderer.setCWC(CWC_LongestLine())

        return table.renderAsCollection().toList()
    }

    private companion object Header {
        const val GEAR = "Gear"
        const val MILLIS = "Frame delay millis"
    }
}