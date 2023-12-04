package com.github.rmgrimm.adventofcode2023.day04

import com.github.rmgrimm.adventofcode2023.support.readResourceLines
import kotlin.math.pow

data class Scratchcard(
    val id: Int,
    val winningNumbers: Set<Int>,
    val containedNumbers: Set<Int>,
) {
    val matchingNumbers = winningNumbers.intersect(containedNumbers)
    val score = if (matchingNumbers.isEmpty()) {
        0
    } else {
        2.0.pow(matchingNumbers.count() - 1).toInt()
    }
}

fun parseCard(line: String) = Scratchcard(
    id = line.substring("Card ".length..<line.indexOf(':'))
        .trim().toInt(),

    winningNumbers = line.substringAfter(':')
        .substringBefore('|')
        .splitToSequence(' ')
        .filterNot(String::isNullOrBlank)
        .map(String::toInt)
        .toSet(),

    containedNumbers = line.substringAfter('|')
        .splitToSequence(' ')
        .filterNot(String::isNullOrBlank)
        .map(String::toInt)
        .toSet()
)

fun main() {
    println(
        readResourceLines("input")
            .map(::parseCard)
            .sumOf(Scratchcard::score)
    )

    // The correct answer is: 17782
}
