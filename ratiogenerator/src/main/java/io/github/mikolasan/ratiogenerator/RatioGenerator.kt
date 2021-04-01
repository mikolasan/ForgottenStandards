package io.github.mikolasan.ratiogenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception
import java.util.*


fun findConversionRatio(nameMap: Map<ImperialUnitName, ImperialUnit>, inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
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

        val inverseCommonUnit = nameMap[unitName]
        inverseCommonUnit?.let {
            val newRatio = inverseCommonUnit.ratioMap.get(outputUnit.unitName)
            if (newRatio != null)
                return 1.0 / (k * newRatio)
        }
    }

    for ((unitName,k) in outputMap) {
        try {
            val unit = nameMap[unitName]
            if (unit != null) return findConversionRatio(nameMap, inputUnit, unit) * k
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
    return inputValue * findConversionRatio(nameMap, input, output)
}

fun doUnits(name: String, imperialUnits: ImperialUnits) {
    val imperialunit = ClassName("io.github.mikolasan.ratiogenerator", "ImperialUnit")
    val imperialtype = ClassName("io.github.mikolasan.ratiogenerator", "ImperialUnitType")
    val array = ClassName("kotlin", "Array")
            .parameterizedBy(imperialunit)

    val units = imperialUnits.units

    val arrayUnits = units.map { unitFrom ->
        val mapUnits = units.map { unitTo ->
            CodeBlock.builder()
                    .add("%T.%L to %L", unitTo.unitName::class, unitTo.unitName, convertValue(imperialUnits.nameMap, unitTo, unitFrom, 1.0))
                    .build()
        }
        CodeBlock.builder()
                .add("%T(%T.%L, %T.%L, mapOf(\n⇥⇥%L⇤⇤))", imperialunit, imperialtype, unitFrom.type, unitFrom.unitName::class, unitFrom.unitName, mapUnits.joinToCode(separator = ",\n"))
                .build()
    }
    val unitsArray = PropertySpec.builder("units", array)
            .addModifiers(KModifier.OVERRIDE)
            .initializer("arrayOf(\n%L\n)", arrayUnits.joinToCode(separator = ",\n"))
            .build()

    val unitsObject = TypeSpec.objectBuilder(name)
            .superclass(ImperialUnits::class)
            .addProperty(unitsArray)
            .build()
    val kotlinFile = FileSpec.builder("io.github.mikolasan.imperialrussia", name)
            .addType(unitsObject)
            .indent("    ")
            .build()

    kotlinFile.writeTo(System.out)
}

fun main() {
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

    doUnits("LengthUnits", MinLengthUnits)
//    doUnits("VolumeUnits", MinVolumeUnits)
}
