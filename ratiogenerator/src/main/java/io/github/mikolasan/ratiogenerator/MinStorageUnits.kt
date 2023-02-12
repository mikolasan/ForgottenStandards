package io.github.mikolasan.ratiogenerator

object MinStorageUnits : ImperialUnits(
    type = ImperialUnitType.STORAGE,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.BYTE), x(8.0, ImperialUnitName.BIT)),
        eq(x(1.0, ImperialUnitName.BLOCK), x(512.0, ImperialUnitName.BYTE)),
        eq(x(1.0, ImperialUnitName.KIBIBYTE), x(1024.0, ImperialUnitName.BYTE)),
        eq(x(1.0, ImperialUnitName.BLOCK), x(4.0, ImperialUnitName.PACKET)),
    ),
    formulaList = listOf())