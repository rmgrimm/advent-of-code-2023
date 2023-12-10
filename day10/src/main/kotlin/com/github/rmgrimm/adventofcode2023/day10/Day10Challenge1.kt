package com.github.rmgrimm.adventofcode2023.day10

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

enum class Direction(val xDelta: Int, val yDelta: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    fun moveFrom(pos: Pair<Int, Int>) =
        (pos.first + xDelta) to (pos.second + yDelta)
}

val opposites = mapOf(
    Direction.NORTH to Direction.SOUTH,
    Direction.EAST to Direction.WEST,
    Direction.SOUTH to Direction.NORTH,
    Direction.WEST to Direction.EAST
)

enum class PipeType(val symbol: Char, val directions: Set<Direction>) {
    VERTICAL('|', setOf(Direction.NORTH, Direction.SOUTH)),
    HORIZONTAL('-', setOf(Direction.WEST, Direction.EAST)),
    NORTH_AND_EAST('L', setOf(Direction.NORTH, Direction.EAST)),
    NORTH_AND_WEST('J', setOf(Direction.NORTH, Direction.WEST)),
    SOUTH_AND_WEST('7', setOf(Direction.WEST, Direction.SOUTH)),
    SOUTH_AND_EAST('F', setOf(Direction.EAST, Direction.SOUTH)),
    GROUND('.', emptySet()),
    START('S', emptySet())
}

val pipeTypeBySymbol = PipeType.entries.associateBy { it.symbol }

data class ParsedMap(
    val startPos: Pair<Int, Int>,
    val map: Array<Array<PipeType>>
) {
    val startDirection = sequenceOf(
        Direction.NORTH,
        Direction.EAST,
        Direction.SOUTH,
        Direction.WEST
    )
        .first { direction ->
            opposites[direction] in pipeAt(
                direction.moveFrom(
                    startPos
                )
            ).directions
        }

    fun pipeAt(pos: Pair<Int, Int>) =
        map[pos.second][pos.first]
}

fun parseMap(inputName: String): ParsedMap {
    val lines = readResourceLines(inputName)
        .toList()

    var startPos: Pair<Int, Int> = 0 to 0

    val output = Array(size = lines.size) { y ->
        val line = lines[y]
        Array(size = line.length) { x ->
            (pipeTypeBySymbol[line[x]] ?: PipeType.GROUND)
                .also {
                    if (it == PipeType.START) {
                        startPos = x to y
                    }
                }
        }
    }

    return ParsedMap(
        startPos = startPos,
        map = output
    )
}

fun main() {
    val parsedMap = parseMap("input")

    var currentPos = parsedMap.startPos
    var nextDirection = parsedMap.startDirection
    var moveCount = 0

    do {
        currentPos = nextDirection.moveFrom(currentPos)
        val pipe = parsedMap.pipeAt(currentPos)
        val movedFromDirection = opposites[nextDirection]

        for (possibleDirection in pipe.directions) {
            if (possibleDirection != movedFromDirection) {
                nextDirection = possibleDirection
            }
        }
        moveCount++
    } while (currentPos != parsedMap.startPos)

    println(moveCount / 2)

    // The correct answer is: 6717
}
