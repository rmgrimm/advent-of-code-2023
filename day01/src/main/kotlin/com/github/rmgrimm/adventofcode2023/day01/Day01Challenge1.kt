package com.github.rmgrimm.adventofcode2023.day01

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")!!
            .map { line ->
                line.first(Char::isDigit).toString() +
                    line.last(Char::isDigit)
            }
            .sumOf(String::toInt)
    )

    // Correct result is: 56506
}
