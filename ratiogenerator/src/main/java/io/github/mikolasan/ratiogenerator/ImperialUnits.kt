package io.github.mikolasan.ratiogenerator

abstract class ImperialUnits() {
    abstract val units: Array<ImperialUnit>
    open val nameMap: Map<ImperialUnitName, ImperialUnit> = emptyMap()

    protected fun makeUnitByNameMap(units: Array<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> {
        val map: MutableMap<ImperialUnitName, ImperialUnit> = mutableMapOf()
        units.forEach {
            map[it.unitName] = it
        }
        return map.toMap()
    }
}