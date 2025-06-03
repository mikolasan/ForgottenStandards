package io.github.mikolasan.ratiogenerator

typealias x<A, B> = Pair<A, B>
typealias eq<A, B> = Pair<A, B>
typealias f<A, B, C> = Triple<A, B, C>
typealias r<A, B> = Pair<A, B>
typealias RatioList = List<eq<x<Double, ImperialUnitName>, x<Double, ImperialUnitName>>>
typealias FormulaList = List<f<ImperialUnitName, String, ImperialUnitName>>
typealias RangeList = List<eq<r<Double, Double>, r<Double, String>>>
typealias RangeListList = List<f<ImperialUnitName, ImperialUnitName, RangeList>>

abstract class ImperialUnitCategory(val type: ImperialUnitType,
                                    val ratioList: RatioList,
                                    val formulaList: FormulaList,
    val rangeList: RangeListList = mutableListOf()
) {

    val units: Set<ImperialUnit> = getAllUnits(ratioList, formulaList, rangeList)
    val nameMap: Map<ImperialUnitName, ImperialUnit> = makeNameMapFromUnits(units)

    init {
        units.forEach { unit ->
            val leftToRight = ratioList
                .filter { it.first.second == unit.unitName }
                .map { it.second.second to (it.first.first / it.second.first) }
            val rightToLeft = ratioList
                .filter { it.second.second == unit.unitName }
                .map { it.first.second to (it.second.first / it.first.first) }
            unit.ratioMap = (leftToRight + rightToLeft).toMap(mutableMapOf())

            val ratiosToFormulae: Map<ImperialUnitName, Array<String>> = unit.ratioMap
                .map { it.key to arrayOf("x * ${it.value}") }
                .toMap()
            val unitFormulae: Map<ImperialUnitName, Array<String>> = formulaList
                .filter { it.first == unit.unitName }
                .associate { it.third to arrayOf(it.second) }
            unit.formulaMap = (ratiosToFormulae + unitFormulae).toMap(mutableMapOf())

            unit.rangeMap = rangeList
                .filter { it.first == unit.unitName }
                .associateTo(mutableMapOf()) { it.second to it.third }
        }
    }

    private fun getAllUnits(ratioList: RatioList, formulaList: FormulaList, rangeList: RangeListList): Set<ImperialUnit> {
        val unitNames: Set<ImperialUnitName> =
            ratioList.flatMap { arrayOf(it.first.second).asIterable() }.toSet() +
                    ratioList.flatMap { arrayOf(it.second.second).asIterable() }.toSet() +
                    formulaList.flatMap { arrayOf(it.first, it.third).asIterable() }.toSet() +
                    rangeList.flatMap { arrayOf(it.first, it.second).asIterable() }.toSet()
        return unitNames.map { name -> ImperialUnit(this, type, name) }.toSet()
    }

    private fun makeNameMapFromUnits(units: Set<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> =
        units.associateBy { it.unitName }

}