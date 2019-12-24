package io.github.mikolasan.imperialrussia

class ImperialUnit(val resourceId: Int, val id: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    var value: Double = 0.0

}