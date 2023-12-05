import kotlin.system.measureTimeMillis

fun main() {

    fun findIndex(mapping: Mapping, index: Long): Long {
        val foundInterval = mapping.mapping.find {
            index >= it[1] && index < it[1] + it[2]
        }
        if (foundInterval != null) {
            return foundInterval[0] + index - foundInterval[1]
        }
        return index
    }

    fun findIndexListed(mapping: Mapping, from: Long, to: Long): List<List<Long>> {
        val res = mutableListOf<List<Long>>()
        var head: Long? = null
        var tail: Long? = null
        var index = from;
        while (index < to) {
            val foundInterval = mapping.mapping.find {
                index >= it[1] && index < it[1] + it[2]
            }
            if (foundInterval != null) {
                //store current interval
                if (head != null && tail != null) {
                    res.add(listOf(head, tail))
                    head = null
                    tail = null
                }
                //if we found an interval assume that all numbers between index and end of the interval
                // will be added to a new result interval
                val newIndex = Math.min(to, foundInterval[1] + foundInterval[2])
                res.add(
                    listOf(
                        foundInterval[0] + index - foundInterval[1],
                        foundInterval[0] + newIndex - foundInterval[1]
                    )
                )
                index = newIndex
                continue
            }
            head = if (head == null) index else head

            // all numbers without found mapping will be added as is until the next interval
            val newIndex = mapping.mapping.fold(Long.MAX_VALUE) { acc, curr ->
                if (curr[1] in index..<acc) curr[1] else acc
            }
            tail = Math.min(to, newIndex) + 1
            index = Math.min(to, newIndex) + 1
        }
        if (head != null && tail != null) {
            res.add(listOf(head, tail))
        }
        return res.toList()
    }

    fun applyMappingToList(mapping: Mapping, list: List<List<Long>>): List<List<Long>> {
        return list.flatMap { findIndexListed(mapping, it.first(), it.last()) }
    }

    fun parseInput(input: List<String>): Almanac {
        val seeds = input[0].substringAfter("seeds: ").split(" ").map { it.toLong() }
        var from = ""
        var to = ""
        val mapping = mutableListOf<List<Long>>()
        val maps = mutableListOf<Mapping>()

        input.drop(1).forEach { line ->
            val matchResult = """([^-]+)-to-([^-]+) map:""".toRegex().find(line)

            if (matchResult != null) {
                if (from != "" && to != "") {
                    maps.add(Mapping(from, to, mapping.toList()))
                }
                from = matchResult.groupValues[1]
                to = matchResult.groupValues[2]
                mapping.clear()
            } else if (line != "") {
                mapping.add(line.split(" ").map { it.toLong() })
            }
        }
        if (from != "" && to != "") {
            maps.add(Mapping(from, to, mapping.toList()))
        }
        return Almanac(seeds, maps.toList())
    }


    fun part1(input: List<String>): Long {
        val almanac = parseInput(input)
        val resultSeeds = almanac.seeds.map { seed ->
            almanac.maps.fold(seed) { acc, curr -> findIndex(curr, acc) }
        }
        return resultSeeds.min()
    }

    fun part2(input: List<String>): Long {
        val almanac = parseInput(input)

        val seedsRange = (0..almanac.seeds.size / 2 - 1).map {
            listOf(
                almanac.seeds[it * 2],
                almanac.seeds[it * 2] + almanac.seeds[it * 2 + 1] - 1
            )
        }
        val result = almanac.maps.fold(seedsRange) { acc, curr -> applyMappingToList(curr, acc) }
        return result.map { it.first() }.min()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    val timeMillis = measureTimeMillis {
        // Your code here
        part2(input).println()
    }

    println("part2 answer found in: ${timeMillis}ms")

}
