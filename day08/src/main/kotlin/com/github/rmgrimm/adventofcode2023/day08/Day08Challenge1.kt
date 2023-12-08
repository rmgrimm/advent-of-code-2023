package com.github.rmgrimm.adventofcode2023.day08

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

data class CamelMap(
    val name: String,
    val left: String,
    val right: String
)

fun parseInputs(lines: List<String>) =
    lines.first() to
        lines.asSequence()
            .drop(1)
            .map { line ->
                val (name, lineDirection) = line.split(" = ", limit = 2)
                val (left, right) = lineDirection.splitToSequence(", ")
                    .map { direction ->
                        direction.replace("(", "")
                            .replace(")", "")
                    }
                    .toList()

                CamelMap(
                    name = name,
                    left = left,
                    right = right
                )
            }
            .associateBy(CamelMap::name)

fun main() {

    val lines = readResourceLines("input")
        .filter(String::isNotBlank)
        .toList()

    val (directions, maps) = parseInputs(lines)

    var currentPath = "AAA"
    val targetLocation = "ZZZ"

    var movementCount = 0L

    while (currentPath != targetLocation) {
        val direction = directions[(movementCount % directions.length).toInt()]

        currentPath = when (direction) {
            'L' -> maps[currentPath]?.left
            'R' -> maps[currentPath]?.right
            else -> null
        } ?: throw IllegalStateException("Moved to unknown position from $currentPath -> $direction")
        movementCount++
    }

    println(movementCount)

    // The correct answer is: 24253
}
