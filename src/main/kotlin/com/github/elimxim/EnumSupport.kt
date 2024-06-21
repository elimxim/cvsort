package com.github.elimxim

fun <T: Enum<T>> Enum<T>.camelCase(): String {
    return this.name.snakeCaseToCamelCase()
}

inline fun <reified T : Enum<T>> sortedEnumValues(
        exclude: Array<T> = emptyArray(),
        ordinal: Boolean = false
): Array<T> {
    val list = if (ordinal) {
        enumValues<T>().sortedBy { it.ordinal }
    } else {
        enumValues<T>().sortedBy { it.name }
    }

    return list.filter { exclude.contains(it).not() }.toTypedArray()
}

inline fun <reified T : Enum<T>> sortedEnumNames(
        exclude: Array<T> = emptyArray(),
        ordinal: Boolean = false,
        camelCase: Boolean = false
): Array<String> {
    return sortedEnumValues<T>(exclude, ordinal)
            .map { if (camelCase) it.name.snakeCaseToCamelCase() else it.name }
            .toTypedArray()
}

inline fun <reified T : Enum<T>> notExistingEnum(
        name: String, camelCase: Boolean = false,
        ignoreCase: Boolean = false
): Boolean {
    return findEnum<T>(name, camelCase, ignoreCase) == null
}

inline fun <reified T : Enum<T>> findEnum(
        name: String, camelCase: Boolean = false,
        ignoreCase: Boolean = false
): T? {
    return enumValues<T>().find { e ->
        if (ignoreCase) {
            if (camelCase) {
                e.name.snakeCaseToCamelCase().equals(name, ignoreCase = true)
            } else
                e.name.equals(name, ignoreCase = true)
        } else if (camelCase) {
            e.name.snakeCaseToCamelCase() == name.replaceFirstChar { it.titlecase() }
        } else {
            e.name == name.uppercase()
        }
    }
}

inline fun <reified T : Enum<T>> getEnumValue(name: String): T {
    var e = findEnum<T>(name, ignoreCase = true)
    if (e == null) {
        e = findEnum<T>(name, camelCase = true)
    }
    return e!!
}