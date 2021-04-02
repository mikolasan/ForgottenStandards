package io.github.mikolasan.ratiogenerator

abstract class ImperialUnits() {
    abstract val units: Array<ImperialUnit>
    abstract val nameMap: Map<ImperialUnitName, ImperialUnit>

    protected fun makeUnitByNameMap(units: Array<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> {
        val map: MutableMap<ImperialUnitName, ImperialUnit> = mutableMapOf()
        units.forEach {
            map[it.unitName] = it
        }
        return map.toMap()
    }
}