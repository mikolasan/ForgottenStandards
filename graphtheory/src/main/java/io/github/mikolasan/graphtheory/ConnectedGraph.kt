package io.github.mikolasan.graphtheory

import org.graphstream.graph.Graph
import org.graphstream.graph.implementations.MultiGraph
import io.github.mikolasan.ratiogenerator.LengthUnits

fun main() {
    System.setProperty("org.graphstream.ui", "swing")
    val graph: Graph = MultiGraph("Length Units")
    graph.isStrict = false
    LengthUnits.lengthUnits.forEach {u ->
        val n = graph.addNode(u.unitName.name)
        n.setAttribute("ui.label", n.id)
    }
    LengthUnits.lengthUnits.forEach {u ->
        u.ratioMap.forEach { (unitName, ratio) ->
            val e = graph.addEdge("${u.unitName.name}->${unitName.name}", u.unitName.name, unitName.name, true)
            e.setAttribute("ui.label", ratio.toString())
        }
    }
    graph.display()
}
