package io.github.mikolasan.imperialrussia

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10


fun valueForDisplay2(value: Double?): String {
    if (value == null) return "-.-"
    val decimalFormat = DecimalFormat()
    decimalFormat.minimumIntegerDigits = 1
    decimalFormat.maximumIntegerDigits = 7
    decimalFormat.isDecimalSeparatorAlwaysShown = false


    return decimalFormat.format(value)
}

fun valueForDisplay(value: Double?, locale: Locale? = null): SpannableStringBuilder {
    if (value == null) return SpannableStringBuilder("-.-")

    val absValue = abs(value)
    val integerPart = floor(absValue)
//    val fractionPart = absValue - integerPart

    val integerLength = if (integerPart > 0) (floor(log10(integerPart)) + 1).toInt() + 1 else 1
    val maxIntegerLength = 7

    val maxDisplayLength = 9

    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat
    if (integerLength > maxIntegerLength) {
        // do scientific notation
        val separator = decimalFormat.decimalFormatSymbols.decimalSeparator
        val exponent = decimalFormat.decimalFormatSymbols.exponentSeparator
        val minus = decimalFormat.decimalFormatSymbols.minusSign
        val pattern = "0" + separator + "###" + exponent + "0"
        decimalFormat.applyLocalizedPattern(pattern)
        var formattedValue = decimalFormat.format(value)
        val minusPosition = formattedValue.indexOf(minus)
        val exponentPosition = formattedValue.indexOf(exponent)
        if (exponentPosition >= 0) {
            formattedValue = formattedValue.replace(exponent, "x10")
            val spannable = SpannableStringBuilder(formattedValue)
            spannable.setSpan(SuperscriptSpan(), exponentPosition + 3, formattedValue.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(0.75f), exponentPosition + 3, formattedValue.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }
    }
    decimalFormat.maximumIntegerDigits = integerLength
    decimalFormat.maximumFractionDigits = maxDisplayLength - integerLength
    decimalFormat.isDecimalSeparatorAlwaysShown = false

    var formattedValue = decimalFormat.format(value)
    val formattedLength = formattedValue.length
    if (formattedLength > maxDisplayLength + 1) {
        // do scientific notation
        val separator = decimalFormat.decimalFormatSymbols.decimalSeparator
        val exponent = decimalFormat.decimalFormatSymbols.exponentSeparator
        val minus = decimalFormat.decimalFormatSymbols.minusSign
        val pattern = "0" + separator + "###" + exponent + "0"
        decimalFormat.applyLocalizedPattern(pattern)
        formattedValue = decimalFormat.format(value)
        val minusPosition = formattedValue.indexOf(minus)
        val exponentPosition = formattedValue.indexOf(exponent)
        if (exponentPosition >= 0) {
            formattedValue = formattedValue.replace(exponent, "x10")
            val spannable = SpannableStringBuilder(formattedValue)
            spannable.setSpan(SuperscriptSpan(), exponentPosition + 3, formattedValue.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(RelativeSizeSpan(0.75f), exponentPosition + 3, formattedValue.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannable
        }
    }

    return SpannableStringBuilder(formattedValue)
}

fun parseDisplayString(string: String, locale: Locale? = null): Double {
    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat
    val number = decimalFormat.parse(string)
    return number?.toDouble() ?: 0.0
}