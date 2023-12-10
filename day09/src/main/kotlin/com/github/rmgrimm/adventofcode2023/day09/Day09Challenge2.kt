package com.github.rmgrimm.adventofcode2023.day09

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")
            .map(::parseAndFindSteps)
            .sumOf { setsOfSteps ->
                setsOfSteps.asReversed()
                    .asSequence()
                    .map { it.first() }
                    .reduce { last, next -> next - last }
            }
    )

    // The correct answer is: 977
}
