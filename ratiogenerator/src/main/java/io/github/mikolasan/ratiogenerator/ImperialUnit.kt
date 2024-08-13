package io.github.mikolasan.ratiogenerator

class ImperialUnit(val category: ImperialUnitCategory,
                   val unitType: ImperialUnitType,
                   val unitName: ImperialUnitName
) {
    var value: Double = 0.0
    var inputString: String = ""
    var formattedString: String = ""
    //var displayString: SpannableStringBuilder = SpannableStringBuilder("-.-")
    var bookmarked = false

    var ratioMap: MutableMap<ImperialUnitName, Double> = mutableMapOf()
    var formulaMap: MutableMap<ImperialUnitName, Array<String>> = mutableMapOf()

    fun restoreValue(s: String, v: Double) {
        inputString = s
        value = v
    }

//    fun inputStringToValue() {
//        value = BasicCalculator(inputString).eval()
//    }
}