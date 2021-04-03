package io.github.mikolasan.graphtheory

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.MultiGraph
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.stream.Collectors

fun main() {
    val units: Array<ImperialUnit> = MinVolumeUnits.units
    System.setProperty("org.graphstream.ui", "swing")
    val graph: Graph = MultiGraph("Imperial Units")
    graph.isStrict = false
    units.forEach { u ->
        val n = graph.addNode(u.unitName.name)
        n.setAttribute("ui.label", n.id)
    }
    units.forEach { u ->
        u.ratioMap.forEach { (unitName, ratio) ->
            val e = graph.addEdge("${u.unitName.name}->${unitName.name}", u.unitName.name, unitName.name, true)
            e.setAttribute("ui.label", ratio.toString())
//            if (ratio.toInt().compareTo(ratio) == 0) {
//                val e = graph.addEdge("${u.unitName.name}->${unitName.name}", u.unitName.name, unitName.name, true)
//                e.setAttribute("ui.label", ratio.toString())
//            }
        }
    }
//    val e = graph.addEdge("CENTIMETER->INCH", "CENTIMETER", "INCH", true)
//    e.setAttribute("ui.label", "2.54")

    val r = object {}.javaClass.getResource("styles.css")
    val f = File("styles.css")
    val fr = FileReader(f)
    val br = BufferedReader(fr)
    val l = br.lines().collect(Collectors.joining());
    val styles = object {}.javaClass.getResource("styles.css")?.readText()
    println(l)
    graph.setAttribute("ui.stylesheet", l)

    graph.display()
}
