package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit

class WorkingUnits(var orderedUnits: Array<ImperialUnit>, var selectedUnit: ImperialUnit, var secondUnit: ImperialUnit) {

    fun getTopUnit(): ImperialUnit {
        val selectedPos = orderedUnits.indexOf(selectedUnit)
        val secondPos = orderedUnits.indexOf(secondUnit)
        return if (selectedPos < secondPos) selectedUnit else secondUnit
    }
}