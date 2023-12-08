package com.github.rmgrimm.adventofcode2023.day07

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

val cardLabelsWithWildcard: String = "J" + (2..9).asSequence()
    .map(Int::digitToChar).joinToString("") + "TQKA"

data class WildCamelCardsHand(
    val hand: String
) : Comparable<WildCamelCardsHand> {
    val cardLabelCounts: Map<Char, Int> = hand.asSequence()
        .groupBy { it }
        .mapValues { (_, values) -> values.count() }

    val type: CamelCardsHandType = when {
        cardLabelCounts.size == 1 -> CamelCardsHandType.FIVE_OF_A_KIND

        cardLabelCounts.size == 2
            && cardLabelCounts.containsKey('J') -> CamelCardsHandType.FIVE_OF_A_KIND

        cardLabelCounts.size == 2
            && cardLabelCounts.containsValue(4) -> CamelCardsHandType.FOUR_OF_A_KIND

        cardLabelCounts.size == 2
            && cardLabelCounts.containsValue(3) -> CamelCardsHandType.FULL_HOUSE

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(3)
            && cardLabelCounts.containsKey('J') -> CamelCardsHandType.FOUR_OF_A_KIND

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(2)
            && cardLabelCounts['J'] == 2 -> CamelCardsHandType.FOUR_OF_A_KIND

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(2)
            && cardLabelCounts['J'] == 1 -> CamelCardsHandType.FULL_HOUSE

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(3) -> CamelCardsHandType.THREE_OF_A_KIND

        cardLabelCounts.size == 3
            && cardLabelCounts.containsValue(2)
            && cardLabelCounts.containsValue(1) -> CamelCardsHandType.TWO_PAIR

        cardLabelCounts.size == 4
            && cardLabelCounts['J'] in 1..2 -> CamelCardsHandType.THREE_OF_A_KIND

        cardLabelCounts.size == 4 -> CamelCardsHandType.ONE_PAIR

        cardLabelCounts.size == 5
            && cardLabelCounts.containsKey('J') -> CamelCardsHandType.ONE_PAIR

        else -> CamelCardsHandType.HIGH_CARD
    }

    override fun compareTo(other: WildCamelCardsHand) = when {
        type == other.type -> {
            (hand.asSequence().map(cardLabelsWithWildcard::indexOf)
                zip
                other.hand.asSequence().map(cardLabelsWithWildcard::indexOf))
                .map { (left, right) -> left.compareTo(right) }
                .filterNot { it == 0 }
                .firstOrNull()
                ?: 0
        }

        else ->
            type.relativeValue.compareTo(other.type.relativeValue)
    }
}

fun parseWildHandAndBid(line: String): Pair<WildCamelCardsHand, Int> =
    line.splitToSequence(" ")
        .filter(String::isNotBlank)
        .toList()
        .let { (hand, bid) -> WildCamelCardsHand(hand) to bid.toInt() }

fun main() {
    println(
        readResourceLines("input")
            .map(::parseWildHandAndBid)
            .sortedBy { (hand, _) -> hand }
            .map { (_, bid) -> bid }
            .mapIndexed { index, bid -> (index + 1) * bid }
            .sum()
    )

    // The correct answer is: 247899149
}

