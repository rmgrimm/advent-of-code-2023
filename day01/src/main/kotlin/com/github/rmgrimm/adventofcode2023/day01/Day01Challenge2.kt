package com.github.rmgrimm.adventofcode2023.day01

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

private val digits =
    listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        .zip(other = (1..9).toList().map(Int::toString))
        .map(Pair<String, String>::toList)

private fun digitValueAndLinePosition(indexFn: (String) -> Int) =
    digits
        .flatMapIndexed { index, digitList ->
            digitList
                .asSequence()
                .map(indexFn)
                .map { (index + 1) to it }
        }
        .filterNot { (_, position) -> position == -1 }

fun main() {
    println(
        readResourceLines("input")!!
            .map { line ->
                val leftDigit = digitValueAndLinePosition(line::indexOf)
                    .minWith(Comparator.comparing(Pair<Int, Int>::second))
                    .first
                val rightDigit = digitValueAndLinePosition(line::lastIndexOf)
                    .maxWith(Comparator.comparing(Pair<Int, Int>::second))
                    .first
                leftDigit.toString() + rightDigit
            }
            .sumOf(String::toInt)
    )

    // Correct result is: 56017
}
