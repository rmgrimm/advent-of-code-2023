package com.github.rmgrimm.adventofcode2023.day05

fun performMapping(
    instructions: MappingInstructions,
    inputRange: LongRange,
    source: String,
    destination: String
): List<LongRange> {
    var currentType = source
    var currentRanges = mutableListOf(inputRange)

    while (currentType != destination) {
        val mappingType = instructions.mapTypeBySource[currentType]
            ?: throw IllegalStateException("No mapping from $currentType; cannot continue")

        val mappings = instructions.maps[mappingType]!!

        val nextRanges = mutableListOf<LongRange>()

        while (currentRanges.isNotEmpty()) {
            val valueRange = currentRanges.removeLast()

            val valueFirst = valueRange.first
            val valueLast = valueRange.last

            var mapped = false

            for (mapping in mappings) {
                val mapFirst = mapping.sourceRange.first
                val mapLast = mapping.sourceRange.last
                val offset = mapping.targetOffset

                // No overlap
                if (valueLast < mapFirst || mapLast < valueFirst) {
                    continue
                }

                mapped = true

                // Full overlap
                if (mapFirst <= valueFirst && valueLast <= mapLast) {
                    nextRanges += (valueFirst + offset)..(valueLast + offset)
                    break
                }

                val internalFirst = if (valueFirst < mapFirst) {
                    currentRanges += valueFirst..<mapFirst
                    mapFirst
                } else {
                    valueFirst
                }

                if (mapLast < valueLast) {
                    nextRanges += (internalFirst + offset)..(mapLast + offset)
                    currentRanges += (mapLast + 1)..valueLast
                } else {
                    nextRanges += (internalFirst + offset)..(valueLast + offset)
                }

                break
            }

            if (!mapped) {
                nextRanges += valueRange
            }
        }

        currentRanges = nextRanges
        currentType = mappingType.destination
    }

    return currentRanges
}

fun main() {
    val (seedRanges, instructions) = parseSeedsAndMaps { seedList ->
        seedList.asSequence()
            .flatMap { seedLine ->
                val (start, length) = seedLine.splitToSequence(' ')
                    .map(String::toLong)
                    .withIndex()
                    .partition { (idx, _) -> idx % 2 == 0 }

                start.map(IndexedValue<Long>::value)
                    .zip(length.map(IndexedValue<Long>::value))
                    .map { (start, length) -> start..<(start + length) }
            }
    }

    println(
        seedRanges
            .flatMap { seedRange ->
                performMapping(
                    instructions = instructions,
                    inputRange = seedRange,
                    source = "seed",
                    destination = "location"
                )
            }
            .minOf(LongRange::first)
    )

    // The correct answer is: 20358599
}
