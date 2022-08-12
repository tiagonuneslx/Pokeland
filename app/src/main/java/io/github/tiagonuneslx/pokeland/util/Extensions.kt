package io.github.tiagonuneslx.pokeland.util

import androidx.compose.ui.Modifier

fun String.capitalizeRoot() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(
        java.util.Locale.ROOT
    ) else it.toString()
}

inline fun Modifier.thenIf(
    condition: Boolean,
    block: () -> Modifier
) = this.then(if (condition) block() else Modifier)