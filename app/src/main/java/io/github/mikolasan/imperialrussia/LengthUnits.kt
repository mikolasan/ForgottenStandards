package io.github.mikolasan.imperialrussia

import java.lang.Exception

object LengthUnits {
    val arshinRatio = mutableMapOf(
            ImperialUnitName.TIP to 0.0625,
            ImperialUnitName.QUARTER to 0.25,
            ImperialUnitName.FOOT to 0.4285714285714286,
            ImperialUnitName.FATHOM to 3.0, // 1 fathom = 3 arshin
            ImperialUnitName.TURN to 1500.0,
            ImperialUnitName.MILE to 10500.0
    )

    val arshin = ImperialUnit(R.string.unit_arshin, ImperialUnitName.YARD, arshinRatio)

    val inchRatio = mutableMapOf(
            ImperialUnitName.TIP to 1.75,
            ImperialUnitName.PALM to 2.9375,
            ImperialUnitName.QUARTER to 7.0,
            ImperialUnitName.FOOT to 12.0,
            ImperialUnitName.YARD to 28.0,
            ImperialUnitName.FATHOM to 84.0, // 1 fathom = 84 inches
            ImperialUnitName.TURN to 42000.0,
            ImperialUnitName.MILE to 294000.0
    )

    val inch = ImperialUnit(R.string.unit_inch, ImperialUnitName.INCH, inchRatio)
    val point = ImperialUnit(R.string.unit_point, ImperialUnitName.POINT, mutableMapOf(ImperialUnitName.INCH to 100.0))
    val line = ImperialUnit(R.string.unit_line, ImperialUnitName.LINE, mutableMapOf(ImperialUnitName.INCH to 10.0))
    val tip = ImperialUnit(R.string.unit_tip, ImperialUnitName.TIP, mutableMapOf(ImperialUnitName.YARD to 16.0))
    val palm = ImperialUnit(R.string.unit_palm, ImperialUnitName.PALM, mutableMapOf(ImperialUnitName.INCH to 16.0/47.0))
    val quarter = ImperialUnit(R.string.unit_quarter, ImperialUnitName.QUARTER, mutableMapOf(ImperialUnitName.YARD to 4.0))
    val foot = ImperialUnit(R.string.unit_foot, ImperialUnitName.FOOT, mutableMapOf(ImperialUnitName.YARD to 7.0/3.0))
    val fathom = ImperialUnit(R.string.unit_fathom, ImperialUnitName.FATHOM, mutableMapOf(ImperialUnitName.YARD to 1.0/3.0))
    val turn = ImperialUnit(R.string.unit_turn, ImperialUnitName.TURN, mutableMapOf(ImperialUnitName.YARD to 1.0/1500.0))
    val mile = ImperialUnit(R.string.unit_mile, ImperialUnitName.MILE, mutableMapOf(ImperialUnitName.YARD to 1.0/10500.0))

    val lengthUnits = arrayOf(point, line, inch, tip, palm, foot, arshin, fathom, turn, mile)

    val imperialUnits = mapOf(
        ImperialUnitName.POINT to point,
        ImperialUnitName.LINE to line,
        ImperialUnitName.INCH to inch,
        ImperialUnitName.TIP to tip,
        ImperialUnitName.PALM to palm,
        ImperialUnitName.QUARTER to quarter,
        ImperialUnitName.FOOT to foot,
        ImperialUnitName.YARD to arshin,
        ImperialUnitName.FATHOM to fathom,
        ImperialUnitName.TURN to turn,
        ImperialUnitName.MILE to mile
    )

    private fun findConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
        val inputMap = inputUnit.ratioMap
        val outputMap = outputUnit.ratioMap
        val ratio = outputMap.get(inputUnit.id)
        if (ratio != null) return ratio

        val inverse = inputMap.get(outputUnit.id)
        if (inverse != null) return 1.0/inverse

        for ((unitName,k) in inputMap) {
            val commonUnitRatio = outputMap.get(unitName)
            commonUnitRatio?.let {
                val newRatio = commonUnitRatio / k
                //outputMap[inputUnit.id] = newRatio
                return newRatio
            }

            val inverseCommonUnit = imperialUnits[unitName]
            inverseCommonUnit?.let {
                val newRatio = inverseCommonUnit.ratioMap.get(outputUnit.id)
                if (newRatio != null)
                    return 1.0 / (k * newRatio)
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
}