package com.github.elimxim.console

import com.github.elimxim.Probe

class ProbeView(private val snap: Probe.Snapshot) {
    fun lines(): List<String> {
        return """<${snap.sortName.canonical()}>
            | iterations   ${snap.iterations}  comparisons  ${snap.comparisons}
            | array reads/writes  ${snap.arrayReads}/${snap.arrayWrites}  ratio  ${"%.2f".format(snap.arrayRatio())}
            | array swaps  ${snap.arraySwaps}
        """.trimMargin().lines()
    }
}