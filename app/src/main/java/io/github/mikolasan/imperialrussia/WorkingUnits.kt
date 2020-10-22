package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit

class WorkingUnits() {
    lateinit var orderedUnits: Array<ImperialUnit>
    lateinit var selectedUnit: ImperialUnit
    lateinit var secondUnit: ImperialUnit
    lateinit var topUnit: ImperialUnit
    lateinit var bottomUnit: ImperialUnit
    lateinit var listAdapter: ImperialListAdapter
}