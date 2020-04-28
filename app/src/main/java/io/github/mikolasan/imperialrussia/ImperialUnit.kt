package io.github.mikolasan.imperialrussia

class ImperialUnit(val displayNameResource: Int, val unitName: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    val type: ImperialUnitType = ImperialUnitType.LENGTH
    var value: Double = 0.0
    var evaluatedString: String = ""
    var formattedString: String = ""

}