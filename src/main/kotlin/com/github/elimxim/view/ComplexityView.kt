package com.github.elimxim.view

import com.github.elimxim.Complexity
import com.github.elimxim.snakeCaseToCamelCase
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciitable.CWC_LongestLine
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment

class ComplexityView : View {
    override fun lines(): List<String> {
        val table = AsciiTable()

        table.addRule()
        table.addRow(listOf(COMPLEXITY, NOTATION))
        table.addRule()

        Complexity.entries.sortedByDescending {
            it.ordinal
        }.forEach {
            table.addRow(listOf(it.name.snakeCaseToCamelCase(), it.notation))
        }

        table.addRule()
        table.setTextAlignment(TextAlignment.LEFT)
        table.setPaddingLeftRight(1, 1)
        table.renderer.setCWC(CWC_LongestLine())

        return table.renderAsCollection().toList()
    }

    private companion object Header {
        const val COMPLEXITY = "Complexity"
        const val NOTATION = "Notation"
    }
}