package io.github.mikolasan.ratiogenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception
import java.util.*
import kotlin.system.measureTimeMillis


fun findUnitByName(name: ImperialUnitName): ImperialUnit? {
    return MinLengthUnits.imperialUnits[name]
}

fun findConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
    val inputMap = inputUnit.ratioMap
    val outputMap = outputUnit.ratioMap
    val ratio = outputMap[inputUnit.unitName]
    if (ratio != null) return ratio

    val inverse = inputMap.get(outputUnit.unitName)
    if (inverse != null) return 1.0/inverse

    for ((unitName,k) in inputMap) {
        val commonUnitRatio = outputMap.get(unitName)
        commonUnitRatio?.let {
            val newRatio = commonUnitRatio / k
            //outputMap[inputUnit.unitName] = newRatio
            return newRatio
        }

        val inverseCommonUnit = findUnitByName(unitName)
        inverseCommonUnit?.let {
            val newRatio = inverseCommonUnit.ratioMap.get(outputUnit.unitName)
            if (newRatio != null)
                return 1.0 / (k * newRatio)
        }
    }

    for ((unitName,k) in outputMap) {
        try {
            val unit = findUnitByName(unitName)
            if (unit != null) return findConversionRatio(inputUnit, unit) * k
        } catch (e: Exception) {

        }
    }

    throw Exception("no ratio")
}

fun printWholeRatios() {
    MinLengthUnits.lengthUnits.forEach { u ->
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
        if (list.size == MinLengthUnits.lengthUnits.size) {
            fullRoutes = fullRoutes.plus<List<ImperialUnit>>(list)
            println(list.map { it.unitName.name }.toString())
        } else {
            val unit = list.last()
            unit.ratioMap.forEach { (unitName, _) ->
                MinLengthUnits.imperialUnits[unitName]?.let { nextUnit ->
                    if (!list.contains(nextUnit)) {
                        makeFullRoute(list.plus(nextUnit))
                    }
                }
            }
            MinLengthUnits.lengthUnits.forEach { nextUnit ->
                if (!list.contains(nextUnit)) {
                    if (nextUnit.ratioMap.containsKey(unit.unitName)) {
                        makeFullRoute(list.plus(nextUnit))
                    }
                }
            }
        }
    }

    MinLengthUnits.lengthUnits.forEach { makeFullRoute(listOf(it)) }
}

fun printFullRoutes2() {
    var fullRoutes: List<List<ImperialUnit>> = Collections.emptyList()

    fun makeFullRoute2(list: List<ImperialUnit>, remainingUnits: List<ImperialUnit>): Unit {
        if (list.size == MinLengthUnits.lengthUnits.size) {
            fullRoutes = fullRoutes.plus<List<ImperialUnit>>(list)
            println(list.map { it.unitName.name }.toString())
        } else {
            val unit = list.last()
            unit.ratioMap
                    .filterKeys { !list.contains(MinLengthUnits.imperialUnits[it]) }
                    .forEach { (unitName, _) ->
                        val nextUnit = MinLengthUnits.imperialUnits.getValue(unitName)
                        makeFullRoute2(list.plus(nextUnit), remainingUnits.minus(nextUnit))
                    }
            remainingUnits.forEach { nextUnit ->
                if (nextUnit.ratioMap.containsKey(unit.unitName)) {
                    makeFullRoute2(list.plus(nextUnit), remainingUnits.minus(nextUnit))
                }
            }
        }
    }

    MinLengthUnits.lengthUnits.forEach { u ->
        val l: List<ImperialUnit> = listOf(u)
        makeFullRoute2(l, MinLengthUnits.lengthUnits.toList().minus(u))
    }
}

fun convertValue(inputUnit: ImperialUnit?, outputUnit: ImperialUnit?, inputValue: Double): Double {
    val input = inputUnit ?: return 0.0
    val output = outputUnit ?: return 0.0
    return inputValue * findConversionRatio(input, output)
}

fun doLength() {
    val imperialunit = ClassName("io.github.mikolasan.ratiogenerator", "ImperialUnit")
    val array = ClassName("kotlin", "Array")
            .parameterizedBy(imperialunit)

    val units = MinLengthUnits.lengthUnits

    val arrayUnits = units.map { unitFrom ->
        val mapUnits = units.map { unitTo ->
            CodeBlock.builder()
                    .add("%T.%L to %L", unitTo.unitName::class, unitTo.unitName, convertValue(unitTo, unitFrom, 1.0))
                    .build()
        }
        CodeBlock.builder()
                .add("%T(%T.%L, mapOf(\n⇥⇥%L⇤⇤))", imperialunit, unitFrom.unitName::class, unitFrom.unitName, mapUnits.joinToCode(separator = ",\n"))
                .build()
    }
    val lengthUnitsArray = PropertySpec.builder("lengthUnits", array)
            .initializer("arrayOf(\n%L\n)", arrayUnits.joinToCode(separator = ",\n"))
            .build()

    val lengthUnitsObject = TypeSpec.objectBuilder("LengthUnits")
            .addProperty(lengthUnitsArray)
            .build()
    val kotlinFile = FileSpec.builder("io.github.mikolasan.imperialrussia", "LengthUnits")
            .addType(lengthUnitsObject)
            .indent("    ")
            .build()

    kotlinFile.writeTo(System.out)
}

fun main() {
//    printWholeRatios()

    val time1 = measureTimeMillis {
        printFullRoutes()
    }
    println("printFullRoutes time: $time1")

    val time2 = measureTimeMillis {
        printFullRoutes2()
    }
    println("printFullRoutes2 time: $time2")

//    doLength()
}