package io.github.mikolasan.ratiogenerator

object MinCookingUnits : ImperialUnitCategory(
    type = ImperialUnitType.COOKING,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.CUP), x(250.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.STAKAN), x(200.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.TABLESPOON), x(15.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.TEASPOON), x(5.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.DASH), x(0.625, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.PINCH), x(0.3125, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.SMIDGEN), x(0.15625, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.DROP), x(0.05, ImperialUnitName.MILLILITER)),
        
        eq(x(1.0, ImperialUnitName.GRAMM), x(1.0, ImperialUnitName.MILLILITER)), // Default: Water
        
        eq(x(1.0, ImperialUnitName.LITER), x(1000.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.KILOGRAMM), x(1000.0, ImperialUnitName.MILLILITER)),
    ),
    formulaList = listOf()
) {
    val ingredients = mapOf(
        "Water" to 1.0,
        "Milk" to 1.03,
        "Flour" to 0.53,
        "Sugar" to 0.85,
        "Butter" to 0.911
    )

    var currentIngredient = "Water"

    fun setIngredient(name: String) {
        val density = ingredients[name] ?: return
        currentIngredient = name
        
        // Update ratio between GRAMM and MILLILITER
        // 1 gram = 1 / density ml
        // 1 ml = density gram
        
        val newRatioList = listOf(
            eq(x(1.0, ImperialUnitName.CUP), x(250.0, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.STAKAN), x(200.0, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.TABLESPOON), x(15.0, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.TEASPOON), x(5.0, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.DASH), x(0.625, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.PINCH), x(0.3125, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.SMIDGEN), x(0.15625, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.DROP), x(0.05, ImperialUnitName.MILLILITER)),
            
            eq(x(1.0, ImperialUnitName.GRAMM), x(1.0 / density, ImperialUnitName.MILLILITER)),
            
            eq(x(1.0, ImperialUnitName.LITER), x(1000.0, ImperialUnitName.MILLILITER)),
            eq(x(1.0, ImperialUnitName.KILOGRAMM), x(1.0 / density * 1000.0, ImperialUnitName.MILLILITER)),
        )
        
        updateRatios(newRatioList)
    }
}