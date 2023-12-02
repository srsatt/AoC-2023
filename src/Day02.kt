class Round// You may want to include a constructor for initialization:
    (var blue: Int, var green: Int, var red: Int) {
}

fun main() {
    fun parseGame(row: String): List<Round> {
        val rounds = """Game \d+: (.+)""".toRegex().find(row)
        return rounds!!.value.split("; ").map {
            val matches = """(\d+) (blue|green|red)""".toRegex().findAll(it)
            val round = Round(0, 0, 0)
            matches.forEach {
                val count = it.groupValues[1].toInt()
                val color = it.groupValues[2]
                when (color) {
                    "blue" -> round.blue += count
                    "green" -> round.green += count
                    "red" -> round.red += count
                }
            }
            round
        }

    }

    fun part1(input: List<String>): Int {
        return input.mapIndexed { index, s ->
            val rounds = parseGame(s)
            if (rounds.all {
                    it.blue <= 14 && it.green <= 13 && it.red <= 12
                }) index + 1 else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val rounds = parseGame(it)
            val minRound = Round(0, 0, 0)
            rounds.forEach {
                minRound.red = Math.max(it.red, minRound.red)
                minRound.blue = Math.max(it.blue, minRound.blue)
                minRound.green = Math.max(it.green, minRound.green)
            }
            minRound.red * minRound.green * minRound.blue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
