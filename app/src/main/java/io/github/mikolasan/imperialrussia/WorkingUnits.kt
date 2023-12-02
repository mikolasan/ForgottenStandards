package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitType

class WorkingUnits() {
    lateinit var allUnits: Map<ImperialUnitType, Array<ImperialUnit>>
    var selectedCategory: ImperialUnitType? = null
    lateinit var orderedUnits: Array<ImperialUnit>
    var favoritedUnits: MutableList<ImperialUnit> = mutableListOf()
    lateinit var topUnit: ImperialUnit
    lateinit var bottomUnit: ImperialUnit
    lateinit var listAdapter: ImperialListAdapter
}