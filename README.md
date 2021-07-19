# Imperial Russia - Historic unit converter

[![codebeat badge](https://codebeat.co/badges/6c7aecaf-b5ce-4dec-b2d3-3e296e5f80e7)](https://codebeat.co/projects/github-com-mikolasan-imperialrussia-master)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/9e76b5d0b6f2445d8518ab9d8f5074f6)](https://www.codacy.com/manual/SaturdaysCode/ImperialRussia?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mikolasan/ImperialRussia&amp;utm_campaign=Badge_Grade)
[![Maintainability](https://api.codeclimate.com/v1/badges/82c2c173a30872b4d741/maintainability)](https://codeclimate.com/github/mikolasan/ImperialRussia/maintainability)

If you are reading a literary book about 18th century, or studying history, or you feel limited with the standard units of measurements, or by any other means find yourself curious about historic units used one or two centuries ago, like when it was time of Russian Empire, then this app might take you interest.

Jump into the new world measured by arshin, versta, sazhen. Measure like Russian.

This app includes simple arithmetic calculator.

Stay tuned for the updates. More features are coming soon.

## How to use Ratio Generator

set unit=Angle
gradle -PimperialUnits=%unit%Units.kt ratiogenerator:runRatioGenerator --args="--className io.github.mikolasan.ratiogenerator.Min%unit%Units --objectName %unit%Units"

```shell
generate_units.bat
```

## Roadmap

- Another unit types: volume, weight
- Slavic calendar
- TODO

## License

MIT
