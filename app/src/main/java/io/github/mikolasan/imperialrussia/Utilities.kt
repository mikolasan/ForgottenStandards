package io.github.mikolasan.imperialrussia

import java.text.DecimalFormat

fun valueForDisplay(value: Double): String {
    val decimalFormat = DecimalFormat()
    if (value >= 1e+9)
        decimalFormat.applyLocalizedPattern("0.0#E0")
    else
        decimalFormat.applyLocalizedPattern("0.0#")

    return decimalFormat.format(value)
}