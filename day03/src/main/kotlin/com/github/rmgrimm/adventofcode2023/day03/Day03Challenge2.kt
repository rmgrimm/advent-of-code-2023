package com.github.rmgrimm.adventofcode2023.day03

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")
            .ifEmpty { sequenceOf("TODO") }
            .first()
    )

    // The correct answer is: ???
}
