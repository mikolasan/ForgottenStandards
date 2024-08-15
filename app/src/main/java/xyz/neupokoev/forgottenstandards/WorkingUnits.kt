package xyz.neupokoev.forgottenstandards

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitType

class WorkingUnits {
    lateinit var allUnits: Map<ImperialUnitType, Array<ImperialUnit>>
    var selectedCategory: ImperialUnitCategoryName? = null
    lateinit var orderedUnits: Array<ImperialUnit>
    var favoriteUnits: MutableList<ImperialUnit> = mutableListOf()
    lateinit var mainUnit: ImperialUnit
}