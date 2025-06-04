package io.github.mikolasan.ratiogenerator

import java.util.Optional

class ImperialUnit(val category: ImperialUnitCategory,
                   val unitType: ImperialUnitType,
                   val unitName: ImperialUnitName
) {
    var value: Double = 0.0
    var inputString: String = ""
    var formattedString: String = ""
    var range: Optional<Range> = Optional.empty()

    //var displayString: SpannableStringBuilder = SpannableStringBuilder("-.-")
    var bookmarked = false

    var ratioMap: MutableMap<ImperialUnitName, Double> = mutableMapOf()
    var formulaMap: MutableMap<ImperialUnitName, Array<String>> = mutableMapOf()
    var rangeUnit: ImperialUnitName = ImperialUnitName.NO_UNIT
    var rangeMap: MutableMap<Double, Range> = mutableMapOf()
    fun restoreValue(s: String, v: Double) {
        inputString = s
        value = v
    }

    // for query outputs, will be assigned in the ContentProvider
    var uniqueId: Long = 0

//    fun inputStringToValue() {
//        value = BasicCalculator(inputString).eval()
//    }
}