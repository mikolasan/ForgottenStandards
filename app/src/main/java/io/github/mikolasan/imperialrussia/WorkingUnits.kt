package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit

class WorkingUnits() {
    lateinit var orderedUnits: Array<ImperialUnit>
    lateinit var selectedUnit: ImperialUnit
    lateinit var secondUnit: ImperialUnit
    lateinit var topUnit: ImperialUnit
    lateinit var bottomUnit: ImperialUnit
    lateinit var listAdapter: ImperialListAdapter

//    fun getTopUnit(): ImperialUnit {
//        val selectedPos = orderedUnits.indexOf(selectedUnit)
//        val secondPos = orderedUnits.indexOf(secondUnit)
//        return if (selectedPos < secondPos) selectedUnit else secondUnit
//    }
}