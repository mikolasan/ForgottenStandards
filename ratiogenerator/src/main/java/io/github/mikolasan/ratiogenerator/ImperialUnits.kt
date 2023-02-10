package io.github.mikolasan.ratiogenerator

typealias x<A, B> = Pair<A, B>
typealias eq<A, B> = Pair<A, B>
typealias f<A, B, C> = Triple<A, B, C>
typealias RatioList = List<eq<x<Double, ImperialUnitName>, x<Double, ImperialUnitName>>>
typealias FormulaList = List<f<ImperialUnitName, String, ImperialUnitName>>

abstract class ImperialUnits(val type: ImperialUnitType,
                             val ratioList: RatioList,
                             val formulaList: FormulaList) {

    val units: Set<ImperialUnit> = getAllUnits(ratioList, formulaList)
    val nameMap: Map<ImperialUnitName, ImperialUnit> = makeNameMapFromUnits(units)

    init {
        if (ratioList.isNotEmpty()) {
            // fill ratio maps
            units.forEach { unit ->
                val leftToRight = ratioList
                    .filter { it.first.second == unit.unitName }
                    .map { it.second.second to (it.first.first / it.second.first) }
                val rightToLeft = ratioList
                    .filter { it.second.second == unit.unitName }
                    .map { it.first.second to (it.second.first / it.first.first) }
                unit.ratioMap = (leftToRight + rightToLeft).toMap()
                unit.formulaMap = unit.ratioMap.map { it.key to arrayOf("x * ${it.value}") }.toMap()
            }
        } else if (formulaList.isNotEmpty()) {
            // fill formula maps
            units.forEach { unit ->
                unit.formulaMap = formulaList
                    .filter { it.first == unit.unitName }
                    .associate { it.third to arrayOf(it.second) }
            }
        }
    }

    private fun getAllUnits(ratioList: RatioList, formulaList: FormulaList): Set<ImperialUnit> {
        val unitNames: Set<ImperialUnitName> =
            if (ratioList.isNotEmpty()) {
                ratioList.flatMap { arrayOf(it.first.second).asIterable() }.toSet() +
                        ratioList.flatMap { arrayOf(it.second.second).asIterable() }.toSet()
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