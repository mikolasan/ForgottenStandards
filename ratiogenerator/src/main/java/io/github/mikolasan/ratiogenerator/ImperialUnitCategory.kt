package io.github.mikolasan.ratiogenerator

typealias x<A, B> = Pair<A, B>
typealias eq<A, B> = Pair<A, B>
typealias f<A, B, C> = Triple<A, B, C>
typealias r<A, B> = Pair<A, B>
typealias RatioList = List<eq<x<Double, ImperialUnitName>, x<Double, ImperialUnitName>>>
typealias FormulaList = List<f<ImperialUnitName, String, ImperialUnitName>>
typealias Range = r<Double, Double>
typealias RangeList = List<eq<Range, r<Double, String>>>
typealias RangeParity = f<ImperialUnitName, ImperialUnitName, RangeList>

abstract class ImperialUnitCategory(val type: ImperialUnitType,
                                    val ratioList: RatioList,
                                    val formulaList: FormulaList,
    val rangeParity: RangeParity = Triple(ImperialUnitName.NO_UNIT, ImperialUnitName.NO_UNIT, listOf())
) {

    val units: Set<ImperialUnit> = getAllUnits(ratioList, formulaList, rangeParity)
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

            if (unit.unitName == rangeParity.second && rangeParity.third.isNotEmpty()) {
                unit.rangeUnit = nameMap.get(rangeParity.first)
                unit.rangeMap = rangeParity.third
                    .associateTo(mutableMapOf()) { it.second.first to it.first }
                unit.rangeValueNames = rangeParity.third
                    .associateTo(mutableMapOf()) { it.second.first to it.second.second }
            }
        }
    }

    private fun getAllUnits(ratioList: RatioList, formulaList: FormulaList, rangeParity: RangeParity): Set<ImperialUnit> {
        val names = mutableSetOf<ImperialUnitName>()
        ratioList.forEach {
            names.add(it.first.second)
            names.add(it.second.second)
        }
        formulaList.forEach {
            names.add(it.first)
            names.add(it.third)
        }
        names.add(rangeParity.first)
        names.add(rangeParity.second)
        return names.filter { it != ImperialUnitName.NO_UNIT }
            .map { name -> ImperialUnit(this, type, name) }
            .toSet()
    }

    private fun makeNameMapFromUnits(units: Set<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> =
        units.associateBy { it.unitName }

}