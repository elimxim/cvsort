package com.github.elimxim.view

import com.github.elimxim.Probe
import com.github.elimxim.SortName

class ProbeView(
        private val sortName: SortName,
        private val snap: Probe.Snapshot
) : View {
    override fun lines(): List<String> {
        val extraSpaces = "".repeat(10)
        return """<${sortName.canonical()}>
            | iterations   ${snap.iterations}  comparisons  ${snap.comparisons} $extraSpaces
            | array reads/writes  ${snap.arrayReads}/${snap.arrayWrites}  ratio  ${"%.2f".format(snap.arrayRatio())} $extraSpaces
            | array swaps  ${snap.arraySwaps} $extraSpaces
        """.trimMargin().lines()
    }
}