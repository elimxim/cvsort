package com.github.elimxim

@Target(AnnotationTarget.CLASS)
annotation class SortAlgorithm(
        val timeComplexity: TimeComplexity,
        val spaceComplexity: Complexity,
        val pseudoCode: String
)

annotation class TimeComplexity(
        val best: Complexity,
        val average: Complexity,
        val worst: Complexity
)

enum class Complexity(val notation: String) {
    CONST("1"),
    LOGARITHMIC("logn"),
    LINEAR("n"),
    LINEARITHMIC("nlogn"),
    QUADRATIC("n^2"),
    CUBIC("n^3"),
    FACTORIAL("n!")
}

enum class ComplexityClass(val notation: String) {
    BIG_O("O"),
    BIG_THETA("Θ"),
    BIG_OMEGA("Ω"),
}