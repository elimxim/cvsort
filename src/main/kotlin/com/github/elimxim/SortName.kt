package com.github.elimxim

enum class SortName {
    ALL,
    BUBBLE,
    BUCKET,
    SHAKER,
    COMB,
    COUNTING,
    HEAP,
    CYCLE,
    GNOME,
    HOARE_QUICK,
    INSERTION,
    LOMUTO_QUICK,
    MERGE,
    ODD_EVEN,
    PANCAKE,
    RADIX,
    SELECTION,
    SHELL,
    STOOGE,
    TREE;

    fun canonical(): String {
        return camelCase().plus("Sort")
    }
}