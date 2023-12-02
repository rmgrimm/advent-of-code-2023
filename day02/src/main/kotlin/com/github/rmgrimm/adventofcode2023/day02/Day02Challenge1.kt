package com.github.rmgrimm.adventofcode2023.day02

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

val maximumAllowedColors = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun parseColoredCubeGame(line: String): Pair<Int, Map<String, Int>> {
    val gameId = line.substring(
        startIndex = "Game ".length,
        endIndex = line.indexOf(':')
    ).toInt()
    val gameColors = line.substringAfter(':')
        .splitToSequence(';')
        .flatMap { it.splitToSequence(',') }
        .map { it.trim().split(' ', limit = 2) }
        .groupBy(
            keySelector = { (_, color) -> color.lowercase() },
            valueTransform = { (quantity, _) -> quantity.toInt() }
        )
        .mapValues { (_, quantityList) -> quantityList.max() }

    return gameId to gameColors
}

fun main() {
    println(
        readResourceLines("input")
            .map(::parseColoredCubeGame)
            .filter { (_, gameColors) ->
                maximumAllowedColors.keys.containsAll(gameColors.keys)
                    && gameColors.all { (color, quantity) ->
                    quantity <= (maximumAllowedColors[color] ?: -1)
                }
            }
            .sumOf { (gameId, _) -> gameId }
    )

    // The correct answer is: 2486
}
