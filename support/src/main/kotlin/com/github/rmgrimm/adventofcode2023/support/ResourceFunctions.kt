package com.github.rmgrimm.adventofcode2023.support

import java.net.URL
import java.nio.charset.Charset

fun readResource(name: String): URL? =
    object {}.javaClass.classLoader.getResource(name)

fun readResourceLines(
    name: String,
    charset: Charset = Charsets.UTF_8) =
    readResource(name)
        ?.readText(charset)
        ?.lineSequence()
        ?.filter(String::isNotBlank)
