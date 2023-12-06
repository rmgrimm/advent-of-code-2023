package com.github.rmgrimm.adventofcode2023.day06

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

fun main() {
    println(
        readResourceLines("input")
            .map { line ->
                line.substringAfter(':')
                    .splitToSequence(' ')
                    .filter(String::isNotBlank)
                    .map(String::toInt)
            }
            .zipWithNext()
            .flatMap { (timeSeq, distSeq) -> timeSeq zip distSeq }
            .map { (maxTime, thresholdDistance) ->
                (1..<maxTime).asSequence()
                    .map { time ->
                        time to (maxTime - time) * time
                    }
                    .filter { (_, distance) -> distance > thresholdDistance }
                    .count()
            }
            .reduce(Int::times)
    )

    // The correct answer is: 293046
}
