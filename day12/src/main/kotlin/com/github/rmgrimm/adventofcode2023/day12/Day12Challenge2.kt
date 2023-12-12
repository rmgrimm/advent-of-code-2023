package com.github.rmgrimm.adventofcode2023.day12

import com.github.rmgrimm.adventofcode2023.support.readResourceLines
import java.math.BigInteger
import kotlin.streams.asStream

/**
 * Not my best attempt. Challenge 1 was fine, this one takes ages to calculate.
 * I'm sure I'm missing some better method on this.
 */
fun main(args: Array<String>) {
    val debug = args.contains("--debug")
    val skip = !args.contains("--dont-skip-long-running")

    if (skip && !debug) {
        println("<long-running-solution-skipped>")
        return
    }

    println(
        if (debug) {
            "Total: "
        } else {
            ""
        } +
        readResourceLines("input")
            .asStream()
            .parallel()
            .map { line ->
                val (coils, dmgGroups) = line.split(' ', limit = 2)

                List(5) { coils }
                    .joinToString("?") +
                    " " +
                    List(5) { dmgGroups }
                        .joinToString(",")
            }
            .map(::parseCoilLine)
            .map {
                val possibleMatches = it.countPossibleMatches()
                if (debug) {
                    println("${it.states} ${it.expectedDmgGroupSizes}: $possibleMatches")
                }
                possibleMatches
            }
            .reduce(BigInteger::plus)
            .orElse(BigInteger.ZERO)
    )

    // The correct answer is: ??? still running...
}
