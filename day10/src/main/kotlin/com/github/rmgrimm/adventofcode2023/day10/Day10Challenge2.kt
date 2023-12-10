package com.github.rmgrimm.adventofcode2023.day10

import java.awt.geom.Path2D

fun main() {
    val parsedMap = parseMap("input")

    var currentPos = parsedMap.startPos
    var nextDirection = parsedMap.startDirection

    val linePositions: MutableSet<Pair<Int, Int>> = mutableSetOf()

    val shape = Path2D.Double()
    shape.moveTo(currentPos.first.toDouble(), currentPos.second.toDouble())

    do {
        linePositions += currentPos
        shape.lineTo(currentPos.first.toDouble(), currentPos.second.toDouble())

        currentPos = nextDirection.moveFrom(currentPos)
        val pipe = parsedMap.pipeAt(currentPos)
        val movedFromDirection = opposites[nextDirection]

        for (possibleDirection in pipe.directions) {
            if (possibleDirection != movedFromDirection) {
                nextDirection = possibleDirection
            }
        }
    } while (currentPos != parsedMap.startPos)

    shape.closePath()

    println(
        parsedMap.map
            .asSequence()
            .flatMapIndexed { y, xArray ->
                xArray.asSequence()
                    .withIndex()
                    .filter { (x, _) ->
                        !linePositions.contains(x to y) &&
                            shape.contains(x.toDouble(), y.toDouble())
                    }
            }.count()
    )

    // The correct answer is: 381
}
