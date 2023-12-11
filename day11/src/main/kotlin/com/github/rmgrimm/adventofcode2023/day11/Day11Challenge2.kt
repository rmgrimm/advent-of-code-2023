package com.github.rmgrimm.adventofcode2023.day11

import kotlin.math.abs

fun main() {
    val expandedUniverse = readUniverse("input")
        .expandUniverse(999_999)

    val galaxyPairs = findGalaxyPairs(expandedUniverse)

    println(galaxyPairs.sumOf { (fromGalaxy, toGalaxy) ->
        abs(toGalaxy.y - fromGalaxy.y) +
            abs(toGalaxy.x - fromGalaxy.x)
    })

    // The correct answer is: 648458253817
}
