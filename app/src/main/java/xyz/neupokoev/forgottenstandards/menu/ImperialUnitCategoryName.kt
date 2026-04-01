package xyz.neupokoev.forgottenstandards.menu

class ImperialUnitCategoryName(val name: String, val iconRes: Int? = null) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ImperialUnitCategoryName) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}