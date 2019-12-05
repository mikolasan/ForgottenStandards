package io.github.mikolasan.imperialrussia

class ImperialUnit(val name: String, val id: ImperialUnitName, val ratioMap: MutableMap<ImperialUnitName, Double>) {
    var value: Double = 0.0
}