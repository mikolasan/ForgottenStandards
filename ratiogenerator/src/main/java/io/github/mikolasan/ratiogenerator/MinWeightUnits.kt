package io.github.mikolasan.ratiogenerator

object MinWeightUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4,
                    ImperialUnitName.SCRUPUL to 28.0,
                    ImperialUnitName.FUNT to 9216.0,
                    ImperialUnitName.ZOLOTNIK to 96.0,
                    ImperialUnitName.LOT to 288.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.ZOLOTNIK, mutableMapOf(
                    ImperialUnitName.FUNT to 96.0,
                    ImperialUnitName.POUND to 84.0,
                    ImperialUnitName.UNTSIYA to 7.0,
                    ImperialUnitName.DOLYA to 1.0/96.0,
                    ImperialUnitName.LOT to 3.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.LOT, mutableMapOf(
                    ImperialUnitName.FUNT to 32.0,
                    ImperialUnitName.DOLYA to 1.0/288.0,
                    ImperialUnitName.ZOLOTNIK to 1.0/3.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.FUNT, mutableMapOf(
                    ImperialUnitName.DOLYA to 1.0/9216.0,
                    ImperialUnitName.LOT to 1.0/96.0,
                    ImperialUnitName.ZOLOTNIK to 1.0/32.0,
                    ImperialUnitName.POOD to 40.0,
                    ImperialUnitName.BERKOVETS to 400.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.POOD, mutableMapOf(
                    ImperialUnitName.FUNT to 1.0/40.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.BERKOVETS, mutableMapOf(
                    ImperialUnitName.POOD to 0.1
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.GRAN, mutableMapOf(
                    ImperialUnitName.SCRUPUL to 20.0,
                    ImperialUnitName.DRAKHMA to 60.0,
                    ImperialUnitName.UNTSIYA to 480.0,
                    ImperialUnitName.POUND to 5760.0,
                    ImperialUnitName.DOLYA to 1.0/1.4
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.SCRUPUL, mutableMapOf(
                    ImperialUnitName.GRAN to 0.05
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.DRAKHMA, mutableMapOf(
                    ImperialUnitName.UNTSIYA to 8.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.UNTSIYA, mutableMapOf(
                    ImperialUnitName.POUND to 12.0
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.KILOGRAMM, mutableMapOf(
                    ImperialUnitName.POOD to 16.3807
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.GRAMM, mutableMapOf(
                    ImperialUnitName.UNTSIYA to 29.861,
                    ImperialUnitName.FUNT to 409.51718,
                    ImperialUnitName.SCRUPUL to 1.2442
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.POUND, mutableMapOf(
                    ImperialUnitName.POOD to 36.121
            )),
            ImperialUnit(ImperialUnitType.WEIGHT, ImperialUnitName.STONE, mutableMapOf(
                    ImperialUnitName.BERKOVETS to 25.8
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}