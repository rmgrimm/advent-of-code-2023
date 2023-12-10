package com.github.rmgrimm.adventofcode2023.day09

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun parseAndFindSteps(line: String) =
    line.splitToSequence(' ')
        .map(String::toInt)
        .toList()
        .let { startSequence ->
            val sequences = mutableListOf(startSequence)

            while (!sequences.last().all { it == 0 }) {
                sequences += sequences.last()
                    .zipWithNext { left, right -> right - left }
            }

            sequences
        }

fun main() {
    println(
        readResourceLines("input")
            .map(::parseAndFindSteps)
            .sumOf { setsOfSteps ->
                setsOfSteps.asReversed()
                    .asSequence()
                    .map { it.last() }
                    .reduce { last, next -> last + next }
            }
    )

    // The correct answer is: 1980437560
}
