package com.github.rmgrimm.adventofcode2023.day08

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {

    val lines = readResourceLines("input")
        .filter(String::isNotBlank)
        .toList()

    val (directions, maps) = parseInputs(lines)

    val directionReaders = directions.asSequence()
        .map { direction ->
            when (direction) {
                'L' -> CamelMap::left
                else -> CamelMap::right
            }
        }
        .toList()
        .toTypedArray()

    println(
        maps.keys.asSequence()
            .filter { it.endsWith('A') }
            .map { start ->
                var current = start
                var count = 0L

                while (!current.endsWith('Z')) {
                    val direction = directionReaders[
                        (count % directionReaders.size).toInt()
                    ]

                    current = direction(maps[current]!!)
                    count++
                }

                count
            }
            .reduce(::lowestCommonMultiple)
    )

    // The correct answer is: 12357789728873
}

fun lowestCommonMultiple(left: Long, right: Long): Long =
    (left * right) / greatestCommonFactor(left, right)

fun greatestCommonFactor(left: Long, right: Long): Long =
    if (right == 0L) {
        left
    } else {
        greatestCommonFactor(right, left % right)
    }
