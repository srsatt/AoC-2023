class Race(val time: Int, val distance: Int)

fun main() {
    fun parseInput(input: List<String>): List<Race> {
        val digits = """(\d+)""".toRegex()
        val times = digits.findAll(input[0]).map { it.value.toInt() }.toList()
        val distances = digits.findAll(input[1]).map { it.value.toInt() }.toList()
        return times.indices.map { Race(times[it], distances[it]) }
    }

    fun part1(input: List<String>): Int {
        val races = parseInput(input)
        val wins = races.map { race ->
            Math.max((0..race.time).filter {
                (race.time - it) * it > race.distance
            }.size, 1)
        }

        return wins.reduce { acc, curr -> acc * curr }
    }

    fun part2(input: List<String>): Int {
        val regex = """(\d+)""".toRegex()
        val time = regex.findAll(input[0]).map { it.value }.joinToString("").toLong()
        val distance = regex.findAll(input[1]).map { it.value }.joinToString("").toLong()
        return (0..time).filter {
            (time - it) * it > distance
        }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
