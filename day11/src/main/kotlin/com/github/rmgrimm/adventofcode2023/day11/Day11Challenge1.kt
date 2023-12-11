package com.github.rmgrimm.adventofcode2023.day11

import com.github.rmgrimm.adventofcode2023.support.readResourceLines
import kotlin.math.abs

data class GalaxyPos(
    val x: Long,
    val y: Long
)

data class Universe(
    val galaxies: List<GalaxyPos>
) {
    val maxX = galaxies.maxOf(GalaxyPos::x)
    val maxY = galaxies.maxOf(GalaxyPos::y)

    val emptyRows = (0..maxY)
        .filterNot { filterY -> galaxies.any { galaxy -> galaxy.y == filterY } }
    val emptyCols = (0..maxX)
        .filterNot { filterX -> galaxies.any { galaxy -> galaxy.x == filterX } }

    fun expandUniverse(distance: Long) = Universe(
        galaxies.map { originalPos ->
            GalaxyPos(
                y = originalPos.y + distance * emptyRows.count { emptyY -> emptyY < originalPos.y },
                x = originalPos.x + distance * emptyCols.count { emptyX -> emptyX < originalPos.x }
            )
        }
    )
}

fun readUniverse(inputName: String) =
    sequence {
        for ((y, line) in readResourceLines(inputName).withIndex()) {
            var x = -1

            while (
                -1 != line.indexOf('#', x + 1)
                    .also { x = it }
            ) {
                yield(GalaxyPos(x.toLong(), y.toLong()))
            }
        }
    }.toList()
        .let { Universe(it) }

fun findGalaxyPairs(expandedUniverse: Universe): MutableSet<Pair<GalaxyPos, GalaxyPos>> {
    val galaxyPairs = mutableSetOf<Pair<GalaxyPos, GalaxyPos>>()

    for (fromGalaxy in expandedUniverse.galaxies) {
        for (toGalaxy in expandedUniverse.galaxies) {
            if (fromGalaxy == toGalaxy) {
                continue
            }

            if (toGalaxy to fromGalaxy in galaxyPairs) {
                continue
            }

            galaxyPairs += fromGalaxy to toGalaxy
        }
    }

    return galaxyPairs
}

fun main() {
    val expandedUniverse = readUniverse("input")
        .expandUniverse(1)

    val galaxyPairs = findGalaxyPairs(expandedUniverse)

    println(galaxyPairs.sumOf { (fromGalaxy, toGalaxy) ->
        abs(toGalaxy.y - fromGalaxy.y) +
            abs(toGalaxy.x - fromGalaxy.x)
    })

    // The correct answer is: 9550717
}
