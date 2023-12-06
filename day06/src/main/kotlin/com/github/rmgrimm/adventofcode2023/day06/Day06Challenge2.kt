package com.github.rmgrimm.adventofcode2023.day06

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")
            .map { line ->
                line.replace(" ", "")
                    .substringAfter(':')
                    .toLong()
            }
            .zipWithNext()
            .map { (maxTime, thresholdDistance) ->
                (1..maxTime).asSequence()
                    .map { time ->
                        (maxTime - time) * time
                    }
                    .filter { distance -> distance > thresholdDistance }
                    .count()
            }
    )

    // The correct answer is: 35150181
}
