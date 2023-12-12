import kotlin.math.abs

fun main() {
    fun normalize(input: List<String>): List<String> {
        val withRows = input.flatMap {
            if (it.toSet().contains('#')) {
                listOf(it)
            } else {
                listOf(it, it)
            }
        }
        val cols = (0..<input.first().length).filter {
            input.all { row -> row[it] == '.' }
        }.toSet()
        return withRows.map {
            it.mapIndexed { index, c ->
                if (cols.contains(index)) {
                    """${c}${c}"""
                } else {
                    c.toString()
                }
            }.joinToString("")
        }
    }

    fun normalize2(input: List<String>, factor: Long): List<Pair<Long, Long>> {
        val rows = (0..<input.size).filter {
            !input[it].toSet().contains('#')
        }
        val cols = (0 until input.first().length).filter { it: Int ->
            input.all { row: String -> row[it] == '.' }
        }

        val stars = input.flatMapIndexed { rowIndex: Int, s: String ->
            s.mapIndexed { colIndex, c ->
                if (c == '#') {
                    val expandCol = cols.filter { col -> col < colIndex }.size
                    val expandRow = rows.filter { row -> row < rowIndex }.size
                    Pair(colIndex + expandCol * (factor - 1), rowIndex + expandRow * (factor - 1))
                } else {
                    null
                }
            }.filterNotNull()
        }
        return stars
    }

    fun distance(first: Pair<Long, Long>, second: Pair<Long, Long>): Long {
        return abs(first.first - second.first) + abs(first.second - second.second)
    }

    fun part1(input: List<String>): Long {
        val normalized = normalize(input)
        val stars = normalized.flatMapIndexed { index: Int, s: String ->
            s.mapIndexed { rowIndex, c ->
                if (c == '#') {
                    Pair(index.toLong(), rowIndex.toLong())
                } else {
                    null
                }
            }.filterNotNull()
        }

        val res = stars.sumOf {
            stars.filter { s -> s != it }.sumOf { s -> distance(it, s) }
        } / 2

        println("""result of part1 is: $res""")
        return res
    }

    fun part2(input: List<String>, factor: Long): Long {
        val stars = normalize2(input, factor)
        val res = stars.sumOf {
            stars.filter { s -> s != it }.sumOf { s -> distance(it, s) }
        } / 2
        println("result of part2 is: $res")
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 2L) == 374L)
    check(part2(testInput, 10L) == 1030L)
    check(part2(testInput, 100L) == 8410L)


    val input = readInput("Day11")
    part1(input).println()
    part2(input, 1000000L).println()
}
