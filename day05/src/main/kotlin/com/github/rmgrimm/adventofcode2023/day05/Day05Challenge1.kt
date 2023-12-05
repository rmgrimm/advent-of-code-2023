package com.github.rmgrimm.adventofcode2023.day05

import com.github.rmgrimm.adventofcode2023.support.readResourceLines

data class MappingType(
    val source: String,
    val destination: String
)

data class MappingInstruction(
    val sourceRange: LongRange,
    val targetOffset: Long
)

data class MappingInstructions(
    val maps: Map<MappingType, List<MappingInstruction>>
) {
    val mapTypeBySource = maps.keys.associateBy(MappingType::source)
}

fun <T> parseSeedsAndMaps(seedsParse: (List<String>) -> T): Pair<T, MappingInstructions> {
    val seeds = mutableListOf<String>()
    val maps = mutableMapOf<MappingType, MutableList<MappingInstruction>>()

    var state = "unknown"
    var mappingType = MappingType(
        source = "unknown",
        destination = "unknown"
    )
    for (line in readResourceLines("input")) {
        var data: String
        val colonPos = line.indexOf(':')
        if (colonPos > -1) {
            val (newState, newLine) = line.split(':', limit = 2)
            state = newState.trim().lowercase()
            data = newLine.trim()

            if (state.endsWith(" map")) {
                val (source, target) = state.substringBefore(" map")
                    .split("-to-", limit = 2)

                mappingType = MappingType(
                    source = source,
                    destination = target
                )

                state = "map"
            }
        } else {
            data = line
        }

        if (data.isBlank()) {
            continue
        }

        when (state) {
            "seeds" -> {
                seeds += data
            }

            "map" -> {
                val (target, source, length) = data.split(' ', limit = 3)
                    .map(String::toLong)
                maps.computeIfAbsent(mappingType) { mutableListOf() } +=
                    MappingInstruction(
                        sourceRange = source..<(source + length),
                        targetOffset = target - source
                    )
            }

            else -> throw UnsupportedOperationException("Unknown state: $state")
        }
    }

    return seedsParse(seeds) to MappingInstructions(maps = maps)
}

fun performMapping(
    instructions: MappingInstructions,
    value: Long,
    source: String,
    destination: String
): Long {
    var currentType = source
    var currentValue = value

    while (currentType != destination) {
        val mappingType = instructions.mapTypeBySource[currentType]
            ?: throw IllegalStateException("No mapping from $currentType; cannot continue")

        currentValue += instructions.maps[mappingType]
            ?.firstOrNull { currentValue in it.sourceRange }
            ?.targetOffset
            ?: 0
        currentType = mappingType.destination
    }

    return currentValue
}

fun main() {
    val (seeds, instructions) = parseSeedsAndMaps { seedList ->
        seedList.asSequence()
            .flatMap { seedLine ->
                seedLine.splitToSequence(' ')
                    .map(String::trim)
                    .map(String::toLong)
            }
    }

    println(
        seeds
            .map { seed ->
                performMapping(
                    instructions = instructions,
                    value = seed,
                    source = "seed",
                    destination = "location"

                )
            }
            .min()
    )

    // The correct answer is: 31599214
}
