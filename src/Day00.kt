fun main() {
    fun part1(input: List<String>): Int {
        val res = input.size
        println("""res of the part 2 is: $res""")
        return res
    }

    fun part2(input: List<String>): Int {
        val res = input.size
        println("""res of the part 2 is: $res""")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
