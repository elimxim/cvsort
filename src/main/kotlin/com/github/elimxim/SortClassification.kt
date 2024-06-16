package com.github.elimxim

@Target(AnnotationTarget.CLASS)
annotation class SortClassification(
        val timeComplexity: TimeComplexity,
        val spaceComplexity: Complexity,
        val methods: Array<Method>,
        val recursive: Boolean,
        val stable: Boolean,
        val pseudoCode: String,
        val extraInfo: ExtraInfo
)

annotation class TimeComplexity(
        val best: Complexity,
        val average: Complexity,
        val worst: Complexity
)

annotation class ExtraInfo(
        val inventionYear: Int = 0,
        val authors: Array<String> = [],
        val wikiUrl: String
)

enum class Complexity(val notation: String) {
    CONST("1"),
    LOGARITHMIC("logn"),
    LINEAR("n"),
    LINEARITHMIC("nlogn"),
    THIRD_QUADRATIC("n^4/3"),
    HALF_QUADRATIC("n^3/2"),
    QUADRATIC("n^2"),
    TWO_THIRDS_CUBIC("n^log3/log1.5"),
    CUBIC("n^3"),
    FACTORIAL("n!")
}

enum class ComplexityClass(val notation: String) {
    BIG_O("O"),
    BIG_THETA("Θ"),
    BIG_OMEGA("Ω"),
}

enum class Method {
    EXCHANGING, SELECTION, INSERTION, MERGING, PARTITIONING;

    fun camelCase(): String {
        return name.lowercase().snakeCaseToCamelCase()
    }
}