fun main() {
    fun ints(card: String): List<Int> {
        val row = card.substringAfter(":").split("|")
        val digits = """(\d+)""".toRegex()

        val winning = digits.findAll(row[0]).map { it.value.toInt() }.toSet()
        val current = digits.findAll(row[1]).map { it.value.toInt() }.toSet()
        val winningNumbers = current.filter {
            winning.contains(it)
        }
        return winningNumbers
    }

    fun part1(input: List<String>): Int {
        val res = input.sumOf {
            val winningNumbers = ints(it)
            if (winningNumbers.isNotEmpty()) Math.pow(2.0, (winningNumbers.size - 1).toDouble()).toInt() else 0

        }
        res.println()
        return res
    }

    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, Int>()

        input.forEachIndexed() { index, card ->
            val winningNumbers = ints(card)
            map.put(index + 1, winningNumbers.size)
        }

        val list = List(map.size) { 1 }.toMutableList()
        list.forEachIndexed { index, count ->
            val round = index + 1
            val winningCount = map.get(round) ?: 0
            if (winningCount > 0) {
                for (winningRound in round..round + winningCount - 1) {
                    if (winningRound < list.size) {
                        list[winningRound] += count
                    }
                }
            }
        }
        return list.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
