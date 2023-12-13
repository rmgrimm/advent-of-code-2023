package com.github.rmgrimm.adventofcode2023.day13

import com.github.rmgrimm.adventofcode2023.support.readGroupedPuzzleInputs

fun parseBitMap(input: List<String>) =
    LongArray(input.size) { index ->
        input[index].replace('#', '1')
            .replace('.', '0')
            .toLong(2)
    }

fun parseTransposedBitMap(input: List<String>) =
    LongArray(input.firstOrNull()?.length ?: 0)
        .also { outputs ->
            for (line in input) {
                for ((index, char) in line.withIndex()) {
                    outputs[index] = (outputs[index] shl 1) +
                        if (char == '#') {
                            1
                        } else {
                            0
                        }
                }
            }
        }

fun findMirrorPoint(puzzle: LongArray) =
    puzzle.asSequence()
        .zipWithNext()
        .mapIndexedNotNull { index, (left, right) ->
            if (left == right) {
                index + 1
            } else {
                null
            }
        }
        .filter { possibleMirrorIndex ->
            (possibleMirrorIndex - 1 downTo 0).map(puzzle::get)
                .zip((possibleMirrorIndex..puzzle.lastIndex).map(puzzle::get))
                .all { (left, right) -> left == right }
        }
        .firstOrNull() ?: 0

fun main() {
    println(
        readGroupedPuzzleInputs("input")
            .sumOf { puzzle ->
                val vertMirrorValue = 100L * findMirrorPoint(parseBitMap(puzzle))
                val horzMirrorValue =
                    findMirrorPoint(parseTransposedBitMap(puzzle))

                horzMirrorValue + vertMirrorValue
            }
    )

    // The correct answer is: 35360
}
