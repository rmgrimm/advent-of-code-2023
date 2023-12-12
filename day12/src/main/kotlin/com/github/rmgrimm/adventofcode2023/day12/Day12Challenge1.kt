package com.github.rmgrimm.adventofcode2023.day12

import com.github.rmgrimm.adventofcode2023.support.readResourceLines
import java.math.BigInteger
import kotlin.math.max

enum class CoilState(val symbol: Char) {
    OPERATIONAL('.'),
    BROKEN('#'),
    UNKNOWN('?');

    companion object {
        val knownEntries = entries
            .filterNot { it == UNKNOWN }
            .toSet()
    }
}

fun MutableListIterator<HotHelixRow>.addNext(row: HotHelixRow) =
    this.apply { this.add(row) }
        .previous()

data class HotHelixRow(
    val states: String,
    val expectedDmgGroupSizes: List<Int>,
    val startingIndex: Int = 0,
    val startingDamageGroupSizes: List<Int> = emptyList()
) {
    private val incomplete: Boolean
    private val beginningMatch: Boolean
    private val remainingCanFit: Boolean
    private val exactDmgGroupMatch: Boolean
    private val damageGroupSizes: List<Int>
    private val nextStartingIndex: Int

    /**
     * Lots of ugliness that shouldn't need to be included in a constructor...
     *
     * This is mostly here to try to cut the time required to calculate for
     * challenge 2.
     */
    init {
        var incompleteIdx = -1
        val damageGroupSizes = startingDamageGroupSizes.toMutableList()

        var damageIdx = if (startingIndex == 0) {
            0
        } else {
            -1
        }
        var lastIndex = 0
        for ((index, coilState) in states.withIndex()) {
            if (index < startingIndex) {
                continue
            }

            lastIndex = index
            when {
                coilState == CoilState.UNKNOWN.symbol -> {
                    incompleteIdx = index
                    break
                }

                damageIdx == -1 && coilState == CoilState.BROKEN.symbol ->
                    damageIdx = index

                damageIdx != -1 && coilState == CoilState.OPERATIONAL.symbol -> {
                    if (damageIdx != index) {
                        damageGroupSizes += index - damageIdx
                    }
                    damageIdx = -1
                }
            }
        }

        val nextGroupMinSize: Int
        if (damageIdx == -1) {
            nextGroupMinSize = -1
            this.nextStartingIndex = lastIndex
        } else {
            if (incompleteIdx == -1) {
                damageGroupSizes += states.length - damageIdx
                this.nextStartingIndex = states.length
                nextGroupMinSize = -1
            } else {
                this.nextStartingIndex = damageIdx
                nextGroupMinSize = lastIndex - damageIdx
            }
        }

        val exactDmgGroupsMatch = damageGroupSizes == expectedDmgGroupSizes

        if (exactDmgGroupsMatch) {
            this.beginningMatch = true
            this.remainingCanFit = true
        } else if (incompleteIdx == -1) {
            this.beginningMatch = false
            this.remainingCanFit = false
        } else {
            val partialDmgGroupExpectation =
                if (damageGroupSizes.size < expectedDmgGroupSizes.size) {
                    expectedDmgGroupSizes.subList(
                        fromIndex = 0,
                        toIndex = damageGroupSizes.size
                    )
                } else {
                    expectedDmgGroupSizes
                }

            val remainingGroupExpectation =
                if (damageGroupSizes.size < expectedDmgGroupSizes.size) {
                    expectedDmgGroupSizes.subList(
                        fromIndex = damageGroupSizes.size,
                        toIndex = expectedDmgGroupSizes.size
                    )
                } else {
                    emptyList()
                }

            val minimumOperational = max(0, remainingGroupExpectation.size - 1)
            val minimumBroken = remainingGroupExpectation.sum()
            val remainingStates = if (damageIdx == -1) {
                states.substring(lastIndex)
            } else {
                states.substring(damageIdx)
            }
            val actualOperational =
                remainingStates.count { it == CoilState.OPERATIONAL.symbol }
            val actualBroken =
                remainingStates.count { it == CoilState.BROKEN.symbol }
            val actualUnknown =
                remainingStates.length - actualOperational - actualBroken

            this.beginningMatch = damageGroupSizes.isEmpty() ||
                damageGroupSizes == partialDmgGroupExpectation
            val nextGroupIsTooBig = (remainingGroupExpectation.firstOrNull() ?: 0) < nextGroupMinSize
            this.remainingCanFit =
                !nextGroupIsTooBig
                    && actualOperational + actualUnknown >= minimumOperational
                    && actualBroken + actualUnknown >= minimumBroken
        }

        this.incomplete = incompleteIdx != -1
        this.exactDmgGroupMatch = exactDmgGroupsMatch
        this.damageGroupSizes = damageGroupSizes
    }

    fun countPossibleMatches(): BigInteger = if (this.exactDmgGroupMatch) {
        BigInteger.ONE
    } else if (!this.beginningMatch) {
        BigInteger.ZERO
    } else {
        explorePossibilities()
            .map(HotHelixRow::exactDmgGroupMatch)
            .fold(BigInteger.ZERO) { accumulator, match ->
                accumulator + if (match) { BigInteger.ONE } else { BigInteger.ZERO }
            }
    }

    fun explorePossibilities(): Sequence<HotHelixRow> = sequence {
        val uncertain = mutableListOf(this@HotHelixRow)
        val uncertainIter = uncertain.listIterator()
        while (uncertainIter.hasNext()) {
            val uncertainRow = uncertainIter.next()
            uncertainIter.remove()

            val nextUnknownIdx =
                uncertainRow.states.indexOf(CoilState.UNKNOWN.symbol)

            if (nextUnknownIdx == -1) {
                yield(uncertainRow)
                continue
            }

            for (possibility in CoilState.knownEntries) {
                val possibilityRow = uncertainRow.copy(
                    states = uncertainRow.states.substring(0..<nextUnknownIdx)
                        + possibility.symbol
                        + uncertainRow.states.substring(nextUnknownIdx + 1),
                    startingIndex = uncertainRow.nextStartingIndex,
                    startingDamageGroupSizes = uncertainRow.damageGroupSizes
                )

                if (possibilityRow.beginningMatch && possibilityRow.remainingCanFit) {
                    uncertainIter.addNext(possibilityRow)
                }
            }
        }
    }

    override fun toString(): String =
        states + " " +
            expectedDmgGroupSizes.joinToString(",")

}

fun parseCoilLine(line: String): HotHelixRow {
    val (stateSymbols, expectationString) = line.split(' ', limit = 2)

    return HotHelixRow(
        states = stateSymbols,
        expectedDmgGroupSizes = expectationString.splitToSequence(',')
            .map(String::toInt)
            .toList()
    )
}

fun main(args: Array<String>) {
    val debug = args.contains("--debug")

    println(
        if (debug) {
            "Total: "
        } else {
            ""
        } +
            readResourceLines("input")
                .map(::parseCoilLine)
                .sumOf {
                    val possibleMatches = it.countPossibleMatches()
                    if (debug) {
                        println("${it.states} ${it.expectedDmgGroupSizes}: $possibleMatches")
                    }
                    possibleMatches
                }
    )

    // The correct answer is: 7402
}
