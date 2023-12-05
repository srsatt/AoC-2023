class Mapping(val from: String, val to: String, val mapping: List<List<Long>>)
class Almanac(val seeds: List<Long>, val maps: List<Mapping>)


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

    fun parseInput(input: List<String>): Almanac {
        val seeds = input[0].substringAfter("seeds: ").split(" ").map { it.toLong() }
        var from = ""
        var to = ""
        val mapping = mutableListOf<List<Long>>()
        val maps = mutableListOf<Mapping>()

        input.drop(1).forEach { line ->
            val matchResult = """([^-]+)-to-([^-]+) map:""".toRegex().find(line)

            if (matchResult != null) {
                maps.add(Mapping(from, to, mapping.toList()))
                from = matchResult.groupValues[1]
                to = matchResult.groupValues[2]
                mapping.clear()
            } else if (line != "") {
                mapping.add(line.split(" ").map { it.toLong() })
            }
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


        val jobs = (0..almanac.seeds.size / 2).map { index ->
            Thread(kotlinx.coroutines.Runnable {
                var res = Long.MAX_VALUE
                for (j in almanac.seeds[index * 2]..almanac.seeds[index * 2] + almanac.seeds[index * 2 + 1]) {
                    res = Math.min(almanac.maps.fold(j) { acc, curr -> findIndex(curr, acc) }, res)
                }
                println("""result for thread $index is, $res""")
            }).start()

        }

        return 0L
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 0L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
