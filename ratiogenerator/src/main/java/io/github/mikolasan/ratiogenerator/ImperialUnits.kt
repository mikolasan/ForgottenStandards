package io.github.mikolasan.ratiogenerator

abstract class ImperialUnits() {
    abstract val units: Array<ImperialUnit>
    abstract val nameMap: Map<ImperialUnitName, ImperialUnit>

    protected fun makeUnitByNameMap(units: Array<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> =
        units.associateBy { it.unitName }

}