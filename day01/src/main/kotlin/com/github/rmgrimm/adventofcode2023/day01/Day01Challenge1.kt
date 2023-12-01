package com.github.rmgrimm.adventofcode2023.day01

import com.github.rmgrimm.adventofcode2023.support.readResource

fun main() {
    println(
        readResource("input")!!
            .readText()
            .lines()
            .asSequence()
            .map(String::trim)
            .filter(String::isNotBlank)
            .map { line ->
                line.first(Char::isDigit).toString() +
                    line.last(Char::isDigit)
            }
            .sumOf(String::toInt)
    )
}
