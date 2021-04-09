@echo off
for %%s in (Angle Area Currency Energy Force Fuel Length Power Pressure Resistance Speed Storage Temperature Time Volume Weight) do (
    echo %%s
    call gradle -PimperialUnits=%%sUnits.kt ratiogenerator:runRatioGenerator --args="--className io.github.mikolasan.ratiogenerator.Min%%sUnits --objectName %%sUnits" --console=plain --quiet
)