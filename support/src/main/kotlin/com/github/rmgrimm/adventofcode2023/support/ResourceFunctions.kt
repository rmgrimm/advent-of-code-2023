package com.github.rmgrimm.adventofcode2023.support

public fun readResource(name: String) =
    object {}.javaClass.classLoader.getResource(name)
