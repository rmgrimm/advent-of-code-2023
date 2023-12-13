package com.github.rmgrimm.adventofcode2023.support

import java.net.URL
import java.nio.charset.Charset

fun readResource(name: String): URL? =
    object {}.javaClass.classLoader.getResource(name)

fun readResourceLines(
    name: String,
    charset: Charset = Charsets.UTF_8
) =
    readResource(name)
        ?.readText(charset)
        ?.lineSequence()
        ?.filter(String::isNotBlank)!!

fun readGroupedPuzzleInputs(
    name: String,
    charset: Charset = Charsets.UTF_8
) = sequence<List<String>> {
    var currentPuzzle = mutableListOf<String>()

    for (line in readResource(name)
        ?.readText(charset)
        ?.lineSequence() ?: emptySequence()) {

        if (line.isBlank()) {
            if (currentPuzzle.isNotEmpty()) {
                yield(currentPuzzle)
                currentPuzzle = mutableListOf()
            }
            continue
        }

        currentPuzzle.add(line)
    }

    if (currentPuzzle.isNotEmpty()) {
        yield(currentPuzzle)
    }
}
