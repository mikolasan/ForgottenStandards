package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitType

class WorkingUnits() {
    lateinit var allUnits: Map<ImperialUnitType, Array<ImperialUnit>>
    lateinit var orderedUnits: Array<ImperialUnit>
//    lateinit var selectedUnit: ImperialUnit
//    lateinit var secondUnit: ImperialUnit
    lateinit var topUnit: ImperialUnit
    lateinit var bottomUnit: ImperialUnit
    lateinit var listAdapter: ImperialListAdapter
}