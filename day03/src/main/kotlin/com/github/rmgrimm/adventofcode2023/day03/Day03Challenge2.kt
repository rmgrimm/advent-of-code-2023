package com.github.rmgrimm.adventofcode2023.day03

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    val (symbolsByPosition, numberPositions) =
        parsePositions(
            readResourceLines("input").toList()
        )

    println(
        symbolsByPosition.asSequence()
            .filter { (_, symbol) -> symbol.value == '*' }
            .map { (pos, _) -> pos }
            .flatMap { pos ->
                numberPositions
                    .filter { numPos ->
                        val yRange = (numPos.y - 1)..(numPos.y + 1)
                        val xRange =
                            (numPos.xRange.first - 1)..(numPos.xRange.last + 1)

                        yRange.contains(pos.y) && xRange.contains(pos.x)
                    }
                    .map { pos to it }
            }
            .groupBy(
                keySelector = { (pos, _) -> pos },
                valueTransform = { (_, number) -> number }
            )
            .values
            .asSequence()
            .filter { numberList -> numberList.size == 2 }
            .sumOf { (n1, n2) -> n1.value * n2.value }
    )

    // The correct answer is: 87605697
}
