package io.github.mikolasan.imperialrussia

class ImperialUnit(val resourceId: Int, val unitName: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    var value: Double? = null

}