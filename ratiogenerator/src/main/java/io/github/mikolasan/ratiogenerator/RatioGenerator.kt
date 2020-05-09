package io.github.mikolasan.ratiogenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.Exception

fun findUnitByName(name: ImperialUnitName): ImperialUnit? {
    return LengthUnits.imperialUnits[name]
}

fun findConversionRatio(inputUnit: ImperialUnit, outputUnit: ImperialUnit): Double {
    val inputMap = inputUnit.ratioMap
    val outputMap = outputUnit.ratioMap
    val ratio = outputMap.get(inputUnit.unitName)
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

fun convertValue(inputUnit: ImperialUnit?, outputUnit: ImperialUnit?, inputValue: Double): Double {
    val input = inputUnit ?: return 0.0
    val output = outputUnit ?: return 0.0
    return inputValue * findConversionRatio(input, output)
}

fun main() {
    val imperialunit = ClassName("io.github.mikolasan.imperialrussia", "ImperialUnit")
    val array = ClassName("kotlin", "Array")
            .parameterizedBy(imperialunit)

    val units = LengthUnits.lengthUnits

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
