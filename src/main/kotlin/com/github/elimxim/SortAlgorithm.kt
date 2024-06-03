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
    LOGARITHMIC("log(n)"),
    LINEAR("n"),
    LINEARITHMIC("nlog(n)"),
    THIRD_QUADRATIC("n^4/3"),
    HALF_QUADRATIC("n^3/2"),
    QUADRATIC("n^2"),
    ALMOST_CUBIC("n^log1.5(3)"),
    CUBIC("n^3"),
    FACTORIAL("n!")
}

enum class ComplexityClass(val notation: String) {
    BIG_O("O"),
    BIG_THETA("Θ"),
    BIG_OMEGA("Ω"),
}