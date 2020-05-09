package io.github.mikolasan.ratiogenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

fun main() {
    val imperialunit = ClassName("io.github.mikolasan.imperialrussia", "ImperialUnit")
    val array = ClassName("", "Array")
            .parameterizedBy(imperialunit)

    val units = listOf("arshin", "inch")
    val codeBlocks = units.map { u ->
        CodeBlock.builder()
                .add("%T(", imperialunit)
                .add("%S,", u)
                .add("%L", ImperialUnitName.ARSHIN)
                .build()
    }
    val lengthUnitsArray = PropertySpec.builder("lengthUnits", array)
            .initializer("arrayOf(%L)", codeBlocks.joinToCode(separator = ","))
            .build()

    val lengthUnitsObject = TypeSpec.objectBuilder("LengthUnits")
            .addProperty(lengthUnitsArray)
            .build()
    val kotlinFile = FileSpec.builder("io.github.mikolasan.imperialrussia", "LengthUnits")
            .addType(lengthUnitsObject)
            .build()

    kotlinFile.writeTo(System.out)
}
