package io.github.mikolasan.imperialrussia

class WorkingUnits(var orderedUnits: MutableList<ImperialUnit>, var selectedUnit: ImperialUnit, var secondUnit: ImperialUnit) {

    fun getTopUnit(): ImperialUnit {
        val selectedPos = orderedUnits.indexOf(selectedUnit)
        val secondPos = orderedUnits.indexOf(secondUnit)
        return if (selectedPos < secondPos) selectedUnit else secondUnit
    }
}