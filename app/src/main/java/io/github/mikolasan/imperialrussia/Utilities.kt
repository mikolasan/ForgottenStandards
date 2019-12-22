package io.github.mikolasan.imperialrussia

import java.text.DecimalFormat

fun valueForDisplay(value: Double): String {
    val decimalFormat = DecimalFormat()
    decimalFormat.minimumIntegerDigits = 1
    decimalFormat.maximumIntegerDigits = 7
    decimalFormat.minimumFractionDigits = 1
    decimalFormat.maximumFractionDigits = 2
    decimalFormat.isDecimalSeparatorAlwaysShown = false


    return decimalFormat.format(value)
}