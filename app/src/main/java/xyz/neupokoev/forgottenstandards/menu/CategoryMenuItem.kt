package xyz.neupokoev.forgottenstandards.menu

import io.github.mikolasan.ratiogenerator.ImperialUnitName

sealed class CategoryMenuItem {
    data class Header(val title: String) : CategoryMenuItem()
    data class Item(val category: ImperialUnitCategoryName) : CategoryMenuItem()
    data class ConversionPair(val unit1: ImperialUnitName, val unit2: ImperialUnitName) : CategoryMenuItem()
}