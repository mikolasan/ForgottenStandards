package io.github.mikolasan.ratiogenerator

typealias x<A, B> = Pair<A, B>
typealias eq<A, B> = Pair<A, B>
typealias f<A, B, C> = Triple<A, B, C>
typealias ConversionTable = Map<x<Double, ImperialUnitName>, x<Double, ImperialUnitName>>
typealias FormulaList = List<f<ImperialUnitName, String, ImperialUnitName>>

abstract class ImperialUnits(val type: ImperialUnitType,
                             val conversionTable: ConversionTable,
                             val formulaList: FormulaList) {

    val units: Set<ImperialUnit> = getAllUnits(conversionTable, formulaList)
    val nameMap: Map<ImperialUnitName, ImperialUnit> = makeNameMapFromUnits(units)

    init {
        // fill ratio maps
        units.forEach { unit ->
            val leftToRight = conversionTable
                .filter { it.key.second == unit.unitName }
                .map { it.key.second to (it.key.first / it.value.first) }
            val rightToLeft = conversionTable
                .filter { it.value.second == unit.unitName }
                .map { it.value.second to (it.value.first / it.key.first)}
            unit.ratioMap = (leftToRight + rightToLeft).toMap()
        }
        // fill formula maps
        units.forEach { unit ->
            unit.formulaMap = formulaList
                .filter { it.first == unit.unitName }
                .associate { it.third to arrayOf(it.second) }
        }
    }

    private fun getAllUnits(conversionTable: ConversionTable, formulaList: FormulaList): Set<ImperialUnit> {
        val unitNames: Set<ImperialUnitName> =
            if (conversionTable.isNotEmpty()) {
                conversionTable.keys.map { it.second }.toSet() + conversionTable.map { it.value.second }.toSet()
            } else if (formulaList.isNotEmpty()) {
                formulaList.flatMap { arrayOf(it.first, it.third).asIterable() }.toSet()
            } else {
                throw Exception("No data to create a list of units")
            }
        return unitNames.map { name -> ImperialUnit(type, name) }.toSet()
    }

    private fun makeNameMapFromUnits(units: Set<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> =
        units.associateBy { it.unitName }

}