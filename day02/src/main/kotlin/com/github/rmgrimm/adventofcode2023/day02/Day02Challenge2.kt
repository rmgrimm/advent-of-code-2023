package com.github.rmgrimm.adventofcode2023.day02

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")
            .map(::parseColoredCubeGame)
            .map { (_, cubeCounts) ->
                cubeCounts.values.reduce(Int::times)
            }
            .sum()
    )

    // Correct answer is: 87984
}
