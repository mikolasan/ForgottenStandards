package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit

class ImperialUnitObserver (var unit: ImperialUnit?) {
    val observers: List<>
    fun getValue(): Double = unit?.value ?: 0.0

    fun setValue(v: Double) {
        unit?.let {
            it.value = v
        }
    }


}