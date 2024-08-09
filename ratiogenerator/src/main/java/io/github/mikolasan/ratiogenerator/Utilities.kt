package io.github.mikolasan.ratiogenerator

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import java.lang.Exception

fun findConversionFormula(nameMap: Map<ImperialUnitName, ImperialUnit>,
                          inputUnit: ImperialUnit,
                          outputUnit: ImperialUnit,
                          searchPath: Array<ImperialUnitName>? = null): Array<String> {
    if (inputUnit.unitName == outputUnit.unitName) return arrayOf("x")
    val inputMap = inputUnit.formulaMap
    val outputMap = outputUnit.formulaMap
    var formulae: Array<String>? = outputMap[inputUnit.unitName]
    if (formulae != null) return formulae

    val inverseFormulae: Array<String>? = inputMap[outputUnit.unitName]
    if (inverseFormulae != null) return FunctionParser().inverse(inverseFormulae)

    val forwardOptions = nameMap
        .mapNotNull { (name, unit) -> if (unit.formulaMap.containsKey(inputUnit.unitName))
                name to unit.formulaMap[inputUnit.unitName]!!
            else
                null
        }
        .toMap()
//    val forwardOptions = nameMap
//        .filter { (_, unit) -> unit.formulaMap.containsKey(inputUnit.unitName) }
//        .map { (name, unit) -> name to unit.formulaMap[inputUnit.unitName]!! }.toMap()

    val inverseOptions = inputMap
        .filter { (name, _) -> !forwardOptions.containsKey(name)}
        .map { (name, formulae) -> name to FunctionParser().inverse(formulae) }
        .toTypedArray()

    val stepOptions: Array<Pair<ImperialUnitName, Array<String>>> = forwardOptions.toList().toTypedArray() + inverseOptions

    if (stepOptions.isEmpty()) {
        throw Exception("no second step")
    }
    val allPaths = stepOptions
        .filter { !(searchPath?.contains(it.first) ?: false) }
        .mapNotNull {
            try {
                var path = searchPath
                if (path.isNullOrEmpty()) {
                    path = arrayOf(inputUnit.unitName)
                }
                val nextSteps =
                    findConversionFormula(
                        nameMap,
                        nameMap[it.first]!!,
                        outputUnit,
                        path + arrayOf(it.first)
                    )
                it.second + nextSteps
            } catch (e: Exception) {
                null
            }
        }
        .sortedBy { it.size }
    formulae = allPaths.first()

    return formulae
}

fun findConversionFormulas(nameMap: Map<ImperialUnitName, ImperialUnit>,
                          inputUnit: ImperialUnit,
                          outputUnit: ImperialUnit,
                          searchPath: Array<ImperialUnitName>? = null): Array<Array<String>> {
    if (inputUnit.unitName == outputUnit.unitName) return arrayOf(arrayOf("x"))
    val inputMap = inputUnit.formulaMap
    val outputMap = outputUnit.formulaMap
    var formulae: Array<String>? = outputMap[inputUnit.unitName]
    if (formulae != null) return arrayOf(formulae)

    val inverseFormulae: Array<String>? = inputMap[outputUnit.unitName]
    if (inverseFormulae != null) return arrayOf(FunctionParser().inverse(inverseFormulae))

    val forwardOptions = nameMap
        .mapNotNull { (name, unit) -> if (unit.formulaMap.containsKey(inputUnit.unitName))
            name to unit.formulaMap[inputUnit.unitName]!!
        else
            null
        }
        .toMap()
//    val forwardOptions = nameMap
//        .filter { (_, unit) -> unit.formulaMap.containsKey(inputUnit.unitName) }
//        .map { (name, unit) -> name to unit.formulaMap[inputUnit.unitName]!! }.toMap()

    val inverseOptions = inputMap
        .filter { (name, _) -> !forwardOptions.containsKey(name)}
        .map { (name, formulae) -> name to FunctionParser().inverse(formulae) }
        .toTypedArray()

    val stepOptions: Array<Pair<ImperialUnitName, Array<String>>> = forwardOptions.toList().toTypedArray() + inverseOptions

    if (stepOptions.isEmpty()) {
        throw Exception("no second step")
    }
    val allPaths = stepOptions
        .filter { !(searchPath?.contains(it.first) ?: false) }
        .mapNotNull {
            try {
                var path = searchPath
                if (path.isNullOrEmpty()) {
                    path = arrayOf(inputUnit.unitName)
                }
                val nextSteps =
                    findConversionFormula(
                        nameMap,
                        nameMap[it.first]!!,
                        outputUnit,
                        path + arrayOf(it.first)
                    )
                it.second + nextSteps
            } catch (e: Exception) {
                null
            }
        }
        .sortedBy { it.size }
    return allPaths.toTypedArray()
}