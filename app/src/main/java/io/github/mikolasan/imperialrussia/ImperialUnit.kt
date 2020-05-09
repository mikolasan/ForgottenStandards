package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnitName

class ImperialUnit(val displayNameResource: Int, val unitName: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    val type: ImperialUnitType = ImperialUnitType.LENGTH
    var value: Double = 0.0
    var inputString: String = ""
    var formattedString: String = ""

    fun inputStringToValue() {
        value = BasicCalculator(inputString).eval()
    }


}