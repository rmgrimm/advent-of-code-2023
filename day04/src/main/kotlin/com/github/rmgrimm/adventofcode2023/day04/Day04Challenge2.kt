package com.github.rmgrimm.adventofcode2023.day04

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    val cardList = readResourceLines("input")
        .map(::parseCard)
        .toList()

    val cardCounts = IntArray(cardList.size) { 1 }

    println(
        cardList.sumOf { card ->
            cardCounts[card.id - 1]
                .also { cardCount ->
                    (card.id..<(card.id + card.matchingNumbers.size))
                        .asSequence()
                        .filter { it <= cardCounts.lastIndex }
                        .forEach { cardCounts[it] += cardCount }
                }
        }
    )

    // The correct answer is: 8477787
}
