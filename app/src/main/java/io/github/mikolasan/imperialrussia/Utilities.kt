package io.github.mikolasan.imperialrussia

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10

fun findUnitByName(name: ImperialUnitName): ImperialUnit? {
    return LengthUnits.imperialUnits[name]
}

fun findConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
    val inputMap = inputUnit.ratioMap
    val outputMap = outputUnit.ratioMap
    val ratio = outputMap.get(inputUnit.unitName)
    if (ratio != null) return ratio

    val inverse = inputMap.get(outputUnit.unitName)
    if (inverse != null) return 1.0/inverse

    for ((unitName,k) in inputMap) {
        val commonUnitRatio = outputMap.get(unitName)
        commonUnitRatio?.let {
            val newRatio = commonUnitRatio / k
            //outputMap[inputUnit.unitName] = newRatio
            return newRatio
        }

        val inverseCommonUnit = findUnitByName(unitName)
        inverseCommonUnit?.let {
            val newRatio = inverseCommonUnit.ratioMap.get(outputUnit.unitName)
            if (newRatio != null)
                return 1.0 / (k * newRatio)
        }
    }

    for ((unitName,k) in outputMap) {
        try {
            val unit = findUnitByName(unitName)
            if (unit != null) return findConversionRatio(inputUnit, unit) * k
        } catch (e: Exception) {

        }
    }

    throw Exception("no ratio")
}

fun convertValue(inputUnit: ImperialUnit?, outputUnit: ImperialUnit?, inputValue: Double): Double {
    val input = inputUnit ?: return 0.0
    val output = outputUnit ?: return 0.0
    return inputValue * findConversionRatio(input, output)
}

const val maxDisplayLength = 9

fun valueForDisplay2(value: Double?): String {
    if (value == null) return "-.-"
    val decimalFormat = DecimalFormat()
    decimalFormat.minimumIntegerDigits = 1
    decimalFormat.maximumIntegerDigits = 7
    decimalFormat.isDecimalSeparatorAlwaysShown = false


    return decimalFormat.format(value)
}

fun doScientificNotation(decimalFormat: DecimalFormat, value: Double): SpannableStringBuilder {
    val separator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val exponent = decimalFormat.decimalFormatSymbols.exponentSeparator
    val minus = decimalFormat.decimalFormatSymbols.minusSign
    val pattern = "0" + separator + "###" + exponent + "0"
    val x10 = "×10"

    decimalFormat.applyLocalizedPattern(pattern)
    var formattedValue = decimalFormat.format(value)
    //val minusPosition = formattedValue.indexOf(minus)
    val exponentPosition = formattedValue.indexOf(exponent)
    if (exponentPosition < 0) {
        // something went wrong, just smile and pretend that it is OK
        return SpannableStringBuilder("^.^")
    }

    formattedValue = formattedValue.replace(exponent, x10)
    val spanStart = exponentPosition + 3
    val spanEnd = formattedValue.length
    val spannable = SpannableStringBuilder(formattedValue)
    spannable.setSpan(SuperscriptSpan(), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(RelativeSizeSpan(0.75f), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}

fun valueForDisplay(value: Double?, locale: Locale? = null): SpannableStringBuilder {
    if (value == null) return SpannableStringBuilder("-.-")

    val absValue = abs(value)
    val integerPart = floor(absValue)
    val integerLength = if (integerPart > 0.0) (floor(log10(integerPart)) + 1).toInt() else 1
    val maxIntegerLength = 8

    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat

    if (integerLength > maxIntegerLength) {
        return doScientificNotation(decimalFormat, value)
    }

    decimalFormat.maximumIntegerDigits = integerLength
    decimalFormat.maximumFractionDigits = maxDisplayLength - integerLength
    decimalFormat.isDecimalSeparatorAlwaysShown = false

    val formattedValue = decimalFormat.format(value)
    val formattedLength = formattedValue.length
    val scientificNumber = doScientificNotation(decimalFormat, value)
    val exponentPosition = scientificNumber.toString().indexOf("×10")
    if (formattedLength > maxDisplayLength + 1) {
        return scientificNumber
    } else if (exponentPosition > 0 && scientificNumber.toString().substring(exponentPosition + 3).toInt() < -7) {
        return scientificNumber
    }

    return SpannableStringBuilder(formattedValue)
}

fun doScientificNotationFormat(format: String, decimalFormat: DecimalFormat, value: Double): SpannableStringBuilder {
    val separator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val exponent = decimalFormat.decimalFormatSymbols.exponentSeparator
    val minus = decimalFormat.decimalFormatSymbols.minusSign
    val pattern = "0" + separator + "###" + exponent + "0"
    val x10 = "×10"

    decimalFormat.applyLocalizedPattern(pattern)
    var formattedValue = decimalFormat.format(value)
    //val minusPosition = formattedValue.indexOf(minus)
    val exponentPosition = formattedValue.indexOf(exponent)
    if (exponentPosition < 0) {
        // something went wrong, just smile and pretend that it is OK
        return SpannableStringBuilder("^.^")
    }

    formattedValue = formattedValue.replace(exponent, x10)
    val valueStart = format.indexOf("[value]")
    val spanStart = valueStart + exponentPosition + 3
    val spanEnd = valueStart + formattedValue.length
    val spannable = SpannableStringBuilder(format.replace("[value]", formattedValue))
    spannable.setSpan(SuperscriptSpan(), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(RelativeSizeSpan(0.75f), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}

fun formatForDisplay(format: String, value: Double?, locale: Locale? = null): SpannableStringBuilder {
    if (value == null) return SpannableStringBuilder(format)

    val absValue = abs(value)
    val integerPart = floor(absValue)
    val integerLength = if (integerPart > 0) (floor(log10(integerPart)) + 1).toInt() else 1
    val maxIntegerLength = 5

    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat

    if (integerLength > maxIntegerLength) {
        return doScientificNotationFormat(format, decimalFormat, value)
    }

    decimalFormat.maximumIntegerDigits = integerLength
    decimalFormat.maximumFractionDigits = maxDisplayLength - integerLength
    decimalFormat.isDecimalSeparatorAlwaysShown = false

    val formattedValue = decimalFormat.format(value)
    val formattedLength = formattedValue.length
    val scientificNumber = doScientificNotation(decimalFormat, value)
    val exponentPosition = scientificNumber.toString().indexOf("×10")
    if (formattedLength > maxDisplayLength + 1) {
        return doScientificNotationFormat(format, decimalFormat, value)
    } else if (exponentPosition > 0 && scientificNumber.toString().substring(exponentPosition + 3).toInt() < -7) {
        return doScientificNotationFormat(format, decimalFormat, value)
    }

    return SpannableStringBuilder(format.replace("[value]", formattedValue))
}

fun parseDisplayString(string: String, locale: Locale? = null): Double {
    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat
    val number = decimalFormat.parse(string)
    return number?.toDouble() ?: 0.0
}

fun <T> MutableList<T>.moveToFrontFrom(index: Int) {
    val tmp = this.removeAt(index)
    this.add(0, tmp)
}

fun <T> MutableList<T>.moveToFront(element: T) {
    this.remove(element)
    this.add(0, element)
}