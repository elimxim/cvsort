package com.github.elimxim

object SortSearcher {
    fun search(attrs: SearchAttributes): List<SortName> {
        return SortName.entries
                .filter { it != SortName.ALL }
                .mapNotNull { SortFactory.classification(it)?.let { c -> Pair(it, c) } }
                .asSequence()
                .filter { pair ->
                    val name = pair.first.name
                    attrs.nameTexts.isEmpty() ||
                            attrs.nameTexts.any { name.contains(it, ignoreCase = true) }
                }
                .filter { pair ->
                    val time = pair.second.timeComplexity
                    attrs.worstTimeComplexity.isEmpty() ||
                            attrs.worstTimeComplexity.any { it == time.worst }
                }
                .filter { pair ->
                    val space = pair.second.spaceComplexity
                    attrs.worstSpaceComplexity.isEmpty() ||
                            attrs.worstSpaceComplexity.any { it == space }
                }
                .filter { pair ->
                    val methods = pair.second.methods
                    attrs.methods.isEmpty() ||
                            (methods.isNotEmpty() && attrs.methods.any {
                                methods.any { m -> m == it }
                            })
                }
                .filter { pair ->
                    val recursive = pair.second.recursive
                    attrs.recursive == null ||
                            attrs.recursive.yesNo(ignoreCase = true).xnor(recursive)
                }
                .filter { pair ->
                    val stable = pair.second.stable
                    attrs.stable == null ||
                            attrs.stable.yesNo(ignoreCase = true).xnor(stable)
                }
                .filter { pair ->
                    val inventionYear = pair.second.extraInfo.inventionYear
                    if (attrs.inventionYears.size == 2) {
                        val from = attrs.inventionYears.minOf { it }
                        val to = attrs.inventionYears.maxOf { it }
                        inventionYear in from..to
                    } else {
                        attrs.inventionYears.isEmpty() ||
                                attrs.inventionYears.any { it == inventionYear }
                    }
                }
                .filter { pair ->
                    val authors = pair.second.extraInfo.authors
                    attrs.authorTexts.isEmpty() ||
                            (authors.isNotEmpty() && attrs.authorTexts.any {
                                authors.any { a -> a.contains(it, ignoreCase = true) }
                            })
                }
                .map { it.first }
                .toList()
    }
}

class SearchAttributes(
        val nameTexts: List<String>,
        val worstTimeComplexity: List<Complexity>,
        val worstSpaceComplexity: List<Complexity>,
        val methods: List<SortMethod>,
        val recursive: String?,
        val stable: String?,
        val inventionYears: List<Int>,
        val authorTexts: List<String>
)