package com.github.elimxim.console.view

import com.github.elimxim.Probe
import com.github.elimxim.SortName

class ProbeView(
        private val sortName: SortName,
        private val snap: Probe.Snapshot
) {
    fun lines(): List<String> {
        return """<${sortName.canonical()}>
            | iterations   ${snap.iterations}  comparisons  ${snap.comparisons}
            | array reads/writes  ${snap.arrayReads}/${snap.arrayWrites}  ratio  ${"%.2f".format(snap.arrayRatio())}
            | array swaps  ${snap.arraySwaps}
        """.trimMargin().lines()
    }
}