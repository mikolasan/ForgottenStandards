package io.github.mikolasan.imperialrussia

import java.lang.Exception
import java.text.DecimalFormat

class ImperialUnit(val name: String, val id: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    var value: Double = 0.0
}

val arshin = ImperialUnit("Arshin", ImperialUnitName.YARD, mutableMapOf(
        ImperialUnitName.FATHOM to 3.0, // 1 fathom = 3 arshin
        ImperialUnitName.TURN to 1500.0,
        ImperialUnitName.MILE to 10500.0))
val inch = ImperialUnit("Inch", ImperialUnitName.INCH, mutableMapOf(ImperialUnitName.YARD to 28.0,
        ImperialUnitName.QUARTER to 7.0,
        ImperialUnitName.FATHOM to 84.0, // 1 fathom = 84 inches
        ImperialUnitName.TURN to 42000.0,
        ImperialUnitName.MILE to 294000.0))
val point = ImperialUnit("Point", ImperialUnitName.POINT, mutableMapOf(ImperialUnitName.INCH to 100.0))
val line = ImperialUnit("Line", ImperialUnitName.LINE, mutableMapOf(ImperialUnitName.INCH to 10.0))
val tip = ImperialUnit("Tip", ImperialUnitName.TIP, mutableMapOf(ImperialUnitName.YARD to 16.0))
val palm = ImperialUnit("Palm", ImperialUnitName.PALM, mutableMapOf(ImperialUnitName.INCH to 16.0/47.0))
val quarter = ImperialUnit("Quarter", ImperialUnitName.QUARTER, mutableMapOf(ImperialUnitName.YARD to 4.0))
val foot = ImperialUnit("Foot", ImperialUnitName.FOOT, mutableMapOf(ImperialUnitName.YARD to 7.0/3.0))
val fathom = ImperialUnit("Fathom", ImperialUnitName.FATHOM, mutableMapOf(ImperialUnitName.YARD to 1.0/3.0))
val turn = ImperialUnit("Turn", ImperialUnitName.TURN, mutableMapOf(ImperialUnitName.YARD to 1.0/1500.0))
val mile = ImperialUnit("Mile", ImperialUnitName.MILE, mutableMapOf(ImperialUnitName.YARD to 1.0/10500.0))

val imperialUnits = mapOf(
        ImperialUnitName.YARD to arshin,
        ImperialUnitName.INCH to inch
)

fun findConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
    val inputMap = inputUnit.ratioMap
    val outputMap = outputUnit.ratioMap
    val ratio = outputMap.get(inputUnit.id)
    if (ratio != null) return ratio

    for ((unitName,k) in inputMap) {
        val commonUnit = outputMap.get(unitName)
        commonUnit?.let {
            val newRatio = commonUnit/k
            outputMap[inputUnit.id] = newRatio
            return newRatio
        }
    }

    for ((unitName,k) in outputMap) {
        try {
            val unit = imperialUnits[unitName]
            if (unit != null) return findConversionRatio(inputUnit, unit) * k
        } catch (e: Exception) {

        }
    }

    throw Exception("no ratio")
}

fun convertValue(inputUnit: ImperialUnit, outputUnit: ImperialUnit, inputValue: Double): Double {
    return inputValue * findConversionRatio(inputUnit, outputUnit)
}

fun valueForDisplay(value: Double): String {
    val decimalFormat = DecimalFormat()
    if (value >= 1e+9)
        decimalFormat.applyLocalizedPattern("0.0#E0")
    else
        decimalFormat.applyLocalizedPattern("0.0#")

    return decimalFormat.format(value)
}