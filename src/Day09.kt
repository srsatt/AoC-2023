fun main() {

    fun descend(row: List<Long>, pickLast: Boolean): List<Long> {
        var curr = row
        val remainders = mutableListOf<Long>()
        while (!curr.all { it == 0L }) {
            remainders.add(if (pickLast) curr.last() else curr.first())
            curr = curr.drop(1).mapIndexed { index, value ->
                value - curr[index]
            }
        }
        return remainders.reversed()
    }

    fun parse(input: List<String>): List<List<Long>> {
        return input.map { it.split(" ").map { it.toLong() } }
    }

    fun part1(input: List<String>): Long {
        val rows = parse(input)
        val result = rows.map { descend(it, true).sum() }.sum()
        return result
    }

    fun part2(input: List<String>): Long {
        val rows = parse(input)
        val result = rows.map { descend(it, false).fold(0L) { acc, l -> l - acc } }
        return result.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
