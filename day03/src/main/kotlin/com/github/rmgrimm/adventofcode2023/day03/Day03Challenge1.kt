package com.github.rmgrimm.adventofcode2023.day03

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

data class Position(
    val y: Int,
    val x: Int
)

data class Symbol(
    val value: Char
)

data class NumberPosition(
    val y: Int,
    val xRange: IntRange,
    val value: Int
)

fun parsePositions(lines: List<String>): Pair<Map<Position, Symbol>, Sequence<NumberPosition>> {
    val digitChars = CharArray(10, Int::digitToChar)

    val symbolsByPosition = lines.asSequence()
        .flatMapIndexed { y, line ->
            line.withIndex()
                .filter { (_, symbol) ->
                    symbol != '.' && !symbol.isDigit()
                }
                .map { (x, char) ->
                    Position(y = y, x = x) to Symbol(value = char)
                }
        }
        .associateBy(
            keySelector = { (pos, _) -> pos },
            valueTransform = { (_, symbol) -> symbol }
        )

    val numberPositions = lines.asSequence()
        .flatMapIndexed { y, line ->
            sequence {
                var nextStart = 0
                var x: Int

                while (
                    -1 != line.indexOfAny(digitChars, startIndex = nextStart)
                        .also { x = it }
                ) {
                    val valueString = line.substring(x).takeWhile(Char::isDigit)

                    nextStart = x + valueString.length

                    yield(
                        NumberPosition(
                            y = y,
                            xRange = x..<nextStart,
                            value = valueString.toInt()
                        )
                    )
                }
            }
        }

    return symbolsByPosition to numberPositions
}

fun main() {
    val (symbolsByPosition, numberPositions) =
        parsePositions(
            readResourceLines("input").toList()
        )

    println(
        numberPositions
            .filter { numPos ->
                val yRange = (numPos.y - 1)..(numPos.y + 1)
                val xRange = (numPos.xRange.first - 1)..(numPos.xRange.last + 1)

                yRange.asSequence()
                    .flatMap { y ->
                        xRange.map { x -> Position(y = y, x = x) }
                    }
                    .any(symbolsByPosition::containsKey)
            }
            .sumOf(NumberPosition::value)
    )

    // The correct answer is: 540212
}
