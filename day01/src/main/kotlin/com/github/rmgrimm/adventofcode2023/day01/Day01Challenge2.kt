package com.github.rmgrimm.adventofcode2023.day01

import com.github.rmgrimm.adventofcode2023.support.readResource

val regexes = listOf(
    "one",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine"
).map(Regex.Companion::fromLiteral)

fun main() {
    println(
        readResource("input")!!
            .readText()
            .let { input ->
                var result = input
                regexes.forEachIndexed { value, regex ->
                    result = result.replace(regex, (value + 1).toString())
                }
                result
            }
            .apply {
                println(this)
            }
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
