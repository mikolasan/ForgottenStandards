package io.github.mikolasan.ratiogenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.mikolasan.convertmeifyoucan.FunctionParser
import java.lang.Exception
import java.util.*

fun addInverseRatios(imperialUnits: ImperialUnits) {
//    val units: Array<ImperialUnit> = imperialUnits.units
//    val nameMap: Map<ImperialUnitName, ImperialUnit> = imperialUnits.nameMap
//    units.forEach { forwardUnit ->
//        forwardUnit.formulaMap?.let { formulaMap ->
//            formulaMap.forEach { (unitName, formula) ->
//                val backwardUnit = nameMap.getValue(unitName)
//                if (backwardUnit.formulaMap == null) {
//                    backwardUnit.formulaMap = mutableMapOf();
//                }
//                when(backwardUnit.formulaMap?.containsKey(forwardUnit.unitName)) {
//                    true -> {
//                        backwardUnit.formulaMap = (backwardUnit.formulaMap :? mapOf()) + Pair(forwardUnit.unitName, "")
//                    }
//                    else -> Unit
//                }
//            }
//        }
//
//        forwardUnit.ratioMap.forEach { (unitName, ratio) ->
//            val backwardUnit = nameMap.getValue(unitName)
//            if (!backwardUnit.ratioMap.containsKey(forwardUnit.unitName)) {
//                backwardUnit.ratioMap += Pair(forwardUnit.unitName, 1.0/ratio)
//            }
//        }
//    }
}

fun findConversionFormula(nameMap: Map<ImperialUnitName, ImperialUnit>,
                          inputUnit: ImperialUnit,
                          outputUnit: ImperialUnit,
                          searchPath: Array<ImperialUnitName>? = null): Array<String> {
    if (inputUnit.unitName == outputUnit.unitName) return arrayOf("x")
    val inputMap = inputUnit.formulaMap ?: throw Exception("no input map")
    val outputMap = outputUnit.formulaMap ?: throw Exception("no output map")
    var formulae: Array<String>? = outputMap[inputUnit.unitName]
    if (formulae != null) return formulae

    val inverseFormulae: Array<String>? = inputMap[outputUnit.unitName]
    if (inverseFormulae != null) return FunctionParser().inverse(inverseFormulae)

    var stepOptions: Array<Pair<ImperialUnitName, Array<String>>> = nameMap
        .filter { (name, unit) -> unit.formulaMap?.containsKey(inputUnit.unitName) ?: false }
        .map { (name, unit) -> name to unit.formulaMap!![inputUnit.unitName]!! }
        .toTypedArray()

    if (stepOptions.isEmpty()) {
        stepOptions = inputMap
            .map { (name, formulae) -> name to FunctionParser().inverse(formulae) }
            .toTypedArray()

    }
    if (stepOptions.isEmpty()) {
        throw Exception("no second step")
    }
    formulae = stepOptions
        .filter { !(searchPath?.contains(it.first) ?: false) }
        .map {
            try {
                var path = searchPath
                if (path.isNullOrEmpty()) {
                    path = arrayOf(inputUnit.unitName)
                }
                val nextSteps =
                    findConversionFormula(nameMap, nameMap[it.first]!!, outputUnit, path + arrayOf(it.first))
                it.second + nextSteps
            } catch (e: Exception) {
                emptyArray()
            }
        }
        .first { (a) -> a.isNotEmpty() } ?: throw Exception("no ratio")

    return formulae
}

fun findConversionRatio(nameMap: Map<ImperialUnitName, ImperialUnit>, inputUnit: ImperialUnit, outputUnit: ImperialUnit, searchPath: Array<ImperialUnitName>? = null): Double {
    if (inputUnit.unitName == outputUnit.unitName) return 1.0
    val inputMap = inputUnit.ratioMap
    val outputMap = outputUnit.ratioMap
    val ratio = outputMap[inputUnit.unitName]
    if (ratio != null) return ratio

    val inverse = inputMap[outputUnit.unitName]
    if (inverse != null) return 1.0/inverse

    for ((unitName,k) in inputMap) {
        if (searchPath != null && searchPath.contains(unitName)) continue

        val commonUnitRatio = outputMap[unitName]
        commonUnitRatio?.let {
            return commonUnitRatio / k
        }

        val inverseCommonUnit = nameMap[unitName]
        inverseCommonUnit?.let {
            val newRatio = inverseCommonUnit.ratioMap[outputUnit.unitName]
            if (newRatio != null)
                return 1.0 / (k * newRatio)
        }
    }

    for ((unitName,k) in outputMap) {
        try {
            val unit = nameMap[unitName]
            if (unit != null) {
                if (searchPath != null && searchPath.contains(unitName)) continue
                return if (searchPath == null) {
                    findConversionRatio(nameMap, inputUnit, unit, arrayOf(unitName)) * k
                } else {
                    findConversionRatio(nameMap, inputUnit, unit, searchPath.plus(unitName)) * k
                }
            }
        } catch (e: Exception) {

        }
    }

    throw Exception("no ratio")
}

fun printWholeRatios() {
    MinLengthUnits.units.forEach { u ->
        u.ratioMap.forEach { (unitName, ratio) ->
            if (ratio.toInt().compareTo(ratio) == 0) {
                println("${u.unitName.name} $ratio -> ${unitName.name}")
            }
        }
    }
}

fun printFullRoutes() {
    var fullRoutes: List<List<ImperialUnit>> = Collections.emptyList()

    fun makeFullRoute(list: List<ImperialUnit>): Unit {
        if (list.size == MinLengthUnits.units.size) {
            fullRoutes = fullRoutes.plus<List<ImperialUnit>>(list)
            println(list.map { it.unitName.name }.toString())
        } else {
            val unit = list.last()
            unit.ratioMap.forEach { (unitName, _) ->
                MinLengthUnits.nameMap[unitName]?.let { nextUnit ->
                    if (!list.contains(nextUnit)) {
                        makeFullRoute(list.plus(nextUnit))
                    }
                }
            }
            MinLengthUnits.units.forEach { nextUnit ->
                if (!list.contains(nextUnit)) {
                    if (nextUnit.ratioMap.containsKey(unit.unitName)) {
                        makeFullRoute(list.plus(nextUnit))
                    }
                }
            }
        }
    }

    MinLengthUnits.units.forEach { makeFullRoute(listOf(it)) }
}

fun printFullRoutes2() {
    var fullRoutes: List<List<ImperialUnit>> = Collections.emptyList()

    fun makeFullRoute2(list: List<ImperialUnit>, remainingUnits: List<ImperialUnit>): Unit {
        if (list.size == MinLengthUnits.units.size) {
            fullRoutes = fullRoutes.plus<List<ImperialUnit>>(list)
            println(list.map { it.unitName.name }.toString())
        } else {
            val unit = list.last()
            unit.ratioMap
                    .filterKeys { !list.contains(MinLengthUnits.nameMap[it]) }
                    .forEach { (unitName, _) ->
                        val nextUnit = MinLengthUnits.nameMap.getValue(unitName)
                        makeFullRoute2(list.plus(nextUnit), remainingUnits.minus(nextUnit))
                    }
            remainingUnits.forEach { nextUnit ->
                if (nextUnit.ratioMap.containsKey(unit.unitName)) {
                    makeFullRoute2(list.plus(nextUnit), remainingUnits.minus(nextUnit))
                }
            }
        }
    }

    MinLengthUnits.units.forEach { u ->
        val l: List<ImperialUnit> = listOf(u)
        makeFullRoute2(l, MinLengthUnits.units.toList().minus(u))
    }
}

fun convertValue(nameMap: Map<ImperialUnitName, ImperialUnit>, inputUnit: ImperialUnit?, outputUnit: ImperialUnit?, inputValue: Double): Double {
    val input = inputUnit ?: return 0.0
    val output = outputUnit ?: return 0.0
    return inputValue * findConversionRatio(nameMap, input, output, null)
}

fun doUnits(name: String, imperialUnits: ImperialUnits) {
    addInverseRatios(imperialUnits)
    val units = imperialUnits.units
    val arrayUnits = units.map { unitFrom ->
        val mapUnits = units.map { unitTo ->
            CodeBlock.builder()
                    .add("%T.%L to %L", unitTo.unitName::class, unitTo.unitName, convertValue(imperialUnits.nameMap, unitTo, unitFrom, 1.0))
                    .build()
        }
        CodeBlock.builder()
                .add("%T(%T.%L, %T.%L, mapOf(\n⇥⇥%L⇤⇤))", ImperialUnit::class, ImperialUnitType::class, unitFrom.type, unitFrom.unitName::class, unitFrom.unitName, mapUnits.joinToCode(separator = ",\n"))
                .build()
    }
    val arrayCodeBlock = arrayUnits.joinToCode(separator = ",\n")
    val unitsArray = PropertySpec.builder("units", Array<ImperialUnit>::class.parameterizedBy(ImperialUnit::class))
            .addModifiers(KModifier.OVERRIDE)
            .initializer("arrayOf(\n%L\n)", arrayCodeBlock)
            .build()
    val nameMap = PropertySpec.builder("nameMap", Map::class.parameterizedBy(ImperialUnitName::class, ImperialUnit::class))
            .addModifiers(KModifier.OVERRIDE)
            .initializer("makeUnitByNameMap(%N)", unitsArray)
            .build()
    val unitsObject = TypeSpec.objectBuilder(name)
            .superclass(ImperialUnits::class)
            .addProperty(unitsArray)
            .addProperty(nameMap)
            .build()
    val kotlinFile = FileSpec.builder("io.github.mikolasan.imperialrussia", name)
            .addType(unitsObject)
            .indent("    ")
            .build()

    kotlinFile.writeTo(System.out)
}

@Suppress("NewApi")
fun main(args: Array<String>) {
    val map = args.fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), elem ->
        if (elem.startsWith("-"))  Pair(map + (elem to emptyList()), elem)
        else Pair(map + (lastKey to map.getOrDefault(lastKey, emptyList()) + elem), lastKey)
    }.first

    val className = map["--className"]?.first() ?: "io.github.mikolasan.ratiogenerator.MinAngleUnits"
    val units = Class.forName(className).kotlin.objectInstance as ImperialUnits
    val name = map["--objectName"]?.first() ?: "AngleUnits"
    doUnits(name, units)
}

//    printWholeRatios()

//    val time1 = measureTimeMillis {
//        printFullRoutes()
//    }
//    println("printFullRoutes time: $time1")
//
//    val time2 = measureTimeMillis {
//        printFullRoutes2()
//    }
//    println("printFullRoutes2 time: $time2")

