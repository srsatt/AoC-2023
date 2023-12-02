fun main() {
    fun part1(input: List<String>): Int {
        val regex = "\\d".toRegex()
        return input.sumOf { row: String ->
            val result = regex.findAll(row).toList()
            """${result.first().value}${result.last().value}""".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val digitMap = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        val regex = """(?=(${digitMap.keys.joinToString(separator = "|")}|\d))""".toRegex()
        return input.sumOf {
            val result = regex.findAll(it).map { it.groups[1]?.value }
            val first = result.first()
            val firstDigit = digitMap[first] ?: first?.toInt()
            val last = result.last()
            val lastDigit = digitMap[last] ?: last?.toInt()

            """${firstDigit}${lastDigit}""".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testInput2 = readInput("Day01_2_test")
    check(part1(testInput) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    println("""part1 answer: ${part1(input).toString()}""")
    println("""part2 answer: ${part2(input).toString()}""")
}
