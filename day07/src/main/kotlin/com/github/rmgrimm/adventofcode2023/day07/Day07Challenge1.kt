package com.github.rmgrimm.adventofcode2023.day07

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

val cardLabels: String = (2..9).asSequence()
    .map(Int::digitToChar).joinToString("") + "TJQKA"

enum class CamelCardsHandType(val relativeValue: Int) {
    HIGH_CARD(0),
    ONE_PAIR(1),
    TWO_PAIR(2),
    THREE_OF_A_KIND(3),
    FULL_HOUSE(4),
    FOUR_OF_A_KIND(5),
    FIVE_OF_A_KIND(6);
}

data class CamelCardsHand(
    val hand: String
) : Comparable<CamelCardsHand> {
    val cardLabelCounts: Map<Char, Int> = hand.asSequence()
        .groupBy { it }
        .mapValues { (_, values) -> values.count() }

    val type: CamelCardsHandType = when {
        cardLabelCounts.size == 1 -> CamelCardsHandType.FIVE_OF_A_KIND

        cardLabelCounts.size == 2
            && cardLabelCounts.containsValue(4) -> CamelCardsHandType.FOUR_OF_A_KIND

        cardLabelCounts.size == 2
            && cardLabelCounts.containsValue(3) -> CamelCardsHandType.FULL_HOUSE

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(3) -> CamelCardsHandType.THREE_OF_A_KIND

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(2)
            && cardLabelCounts.containsValue(1) -> CamelCardsHandType.TWO_PAIR

        cardLabelCounts.size == 4 -> CamelCardsHandType.ONE_PAIR

        else -> CamelCardsHandType.HIGH_CARD
    }

    override fun compareTo(other: CamelCardsHand) = when {
        type == other.type -> {
            (hand.asSequence().map(cardLabels::indexOf)
                    zip
                    other.hand.asSequence().map(cardLabels::indexOf))
                .map { (left, right) -> left.compareTo(right) }
                .filterNot { it == 0 }
                .firstOrNull()
                ?: 0
        }

        else ->
            type.relativeValue.compareTo(other.type.relativeValue)
    }
}

fun parseHandAndBid(line: String): Pair<CamelCardsHand, Int> =
    line.splitToSequence(" ")
        .filter(String::isNotBlank)
        .toList()
        .let { (hand, bid) -> CamelCardsHand(hand) to bid.toInt() }

fun main() {
    println(
        readResourceLines("input")
            .map(::parseHandAndBid)
            .sortedBy { (hand, _) -> hand }
            .map { (_, bid) -> bid }
            .mapIndexed { index, bid -> (index + 1) * bid }
            .sum()
    )

    // The correct answer is: 245794640
}
