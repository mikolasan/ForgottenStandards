package io.github.mikolasan.imperialrussia

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10

fun getConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
    return outputUnit.ratioMap[inputUnit.unitName] ?: throw ImperialConverterException("no ratio")
}

fun convertValue(inputUnit: ImperialUnit?, outputUnit: ImperialUnit?, inputValue: Double): Double {
    val input = inputUnit ?: return 0.0
    val output = outputUnit ?: return 0.0
    return if (inputUnit.type == ImperialUnitType.TEMPERATURE) {
        temperatureFormula(inputUnit.unitName, outputUnit.unitName, inputValue)
    } else {
        inputValue * getConversionRatio(input, output)
    }
}

fun temperatureFormula(inputUnit: ImperialUnitName, outputUnit: ImperialUnitName, inputValue: Double): Double {
    if (inputUnit == outputUnit) {
      return inputValue
    } else if (inputUnit == ImperialUnitName.CELSIUS && outputUnit == ImperialUnitName.FAHRENHEIT) {
        return (inputValue * 9.0 / 5.0) + 32.0
    } else if (inputUnit == ImperialUnitName.FAHRENHEIT && outputUnit == ImperialUnitName.CELSIUS) {
        return (inputValue - 32.0) * 5.0 / 9.0
    } else if (inputUnit == ImperialUnitName.CELSIUS && outputUnit == ImperialUnitName.KELVIN) {
        return inputValue + 273.15
    } else if (inputUnit == ImperialUnitName.KELVIN && outputUnit == ImperialUnitName.CELSIUS) {
        return inputValue - 273.15
    } else if (inputUnit == ImperialUnitName.FAHRENHEIT && outputUnit == ImperialUnitName.KELVIN) {
        val celsiusValue = temperatureFormula(ImperialUnitName.FAHRENHEIT, ImperialUnitName.CELSIUS, inputValue)
        return temperatureFormula(ImperialUnitName.CELSIUS, ImperialUnitName.KELVIN, celsiusValue)
    } else if (inputUnit == ImperialUnitName.KELVIN && outputUnit == ImperialUnitName.FAHRENHEIT) {
        val celsiusValue = temperatureFormula(ImperialUnitName.KELVIN, ImperialUnitName.CELSIUS, inputValue)
        return temperatureFormula(ImperialUnitName.CELSIUS, ImperialUnitName.FAHRENHEIT, celsiusValue)
    }
    return 0.0
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

fun stringForDisplay(formattedValue: String): SpannableStringBuilder {
    val x10 = "×10^"
    val exponentPosition = formattedValue.indexOf(x10)
    if (exponentPosition < 0) {
        return SpannableStringBuilder(formattedValue)
    }
    val noHats = formattedValue.replace("^", "")
    val spanStart = exponentPosition + 3
    val spanEnd = noHats.length
    val spannable = SpannableStringBuilder(noHats)
    spannable.setSpan(SuperscriptSpan(), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(RelativeSizeSpan(0.75f), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
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
    decimalFormat.maximumFractionDigits = maxDisplayLength - integerLength - 1
    decimalFormat.isDecimalSeparatorAlwaysShown = false

    val formattedValue = decimalFormat.format(value)
    val formattedLength = formattedValue.replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(),"").length
    val scientificNumber = doScientificNotation(decimalFormat, value)
    val exponentPosition = scientificNumber.toString().indexOf("×10")
    if (formattedLength > maxDisplayLength + 1) {
        return scientificNumber
    } else if (exponentPosition > 0 && scientificNumber.toString().substring(exponentPosition + 3).toInt() < -7) {
        return scientificNumber
    }

    return SpannableStringBuilder(formattedValue)
}

fun doScientificNotationInPattern(format: String, decimalFormat: DecimalFormat, value: Double): SpannableStringBuilder {
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

fun patternForDisplay(format: String, value: Double?, locale: Locale? = null): SpannableStringBuilder {
    if (value == null) return SpannableStringBuilder(format)

    val absValue = abs(value)
    val integerPart = floor(absValue)
    val integerLength = if (integerPart > 0) (floor(log10(integerPart)) + 1).toInt() else 1
    val maxIntegerLength = 5

    val numberFormat = if (locale == null) DecimalFormat.getInstance() else DecimalFormat.getInstance(locale)
    val decimalFormat = numberFormat as DecimalFormat

    if (integerLength > maxIntegerLength) {
        return doScientificNotationInPattern(format, decimalFormat, value)
    }

    decimalFormat.maximumIntegerDigits = integerLength
    decimalFormat.maximumFractionDigits = maxDisplayLength - integerLength
    decimalFormat.isDecimalSeparatorAlwaysShown = false

    val formattedValue = decimalFormat.format(value)
    val formattedLength = formattedValue.length
    val scientificNumber = doScientificNotation(decimalFormat, value)
    val exponentPosition = scientificNumber.toString().indexOf("×10")
    if (formattedLength > maxDisplayLength + 1) {
        return doScientificNotationInPattern(format, decimalFormat, value)
    } else if (exponentPosition > 0 && scientificNumber.toString().substring(exponentPosition + 3).toInt() < -7) {
        return doScientificNotationInPattern(format, decimalFormat, value)
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

fun <T> Array<T>.moveToFrontFrom(index: Int) {
    val tmp = this[index]
    System.arraycopy(this, 0, this, 1, index)
    this[0] = tmp
}

// Option to change it not in place
//val shifted = this.copyInto(this, 1, 0, index)
//shifted[0] = tmp

fun <T> Array<T>.moveToFront(element: T) {
    val i = this.indexOf(element)
    if (i < 0) return
    this.moveToFrontFrom(i)
}
