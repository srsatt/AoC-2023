import kotlin.math.abs

class Entity(private val row: Int, val raw: String, private val firstCol: Int, private val lastCol: Int) {
    val number = raw.toIntOrNull()
    fun isNeighbour(x: Entity): Boolean {
        val inside: (Int, Int, Int) -> Boolean = { from, to, v ->
            v >= from - 1 && v <= to + 1
        }
        return abs(row - x.row) <= 1 &&
                (inside(firstCol, lastCol, x.firstCol) || inside(firstCol, lastCol, x.lastCol) ||
                        inside(
                            x.firstCol, x.lastCol, firstCol
                        ) || inside(x.firstCol, x.lastCol, lastCol))
    }

    val isNumber = number != null
}

fun main() {

    fun processRow(row: String, rowIndex: Int): MutableList<Entity> {
        val result = mutableListOf<Entity>()
        var curNumber = ""
        val addCurrNumber = { colIndex: Int ->
            if (curNumber != "") {
                val entity = Entity(rowIndex, curNumber, colIndex - curNumber.length + 1, colIndex)
                result.add(entity)
            }
            curNumber = ""
        }
        row.toList().forEachIndexed { colIndex: Int, rawChar: Char ->
            val char = rawChar.toString()
            when {
                char.toIntOrNull() != null -> curNumber += char
                char == "." -> {
                    addCurrNumber(colIndex - 1)
                }

                char.isNotEmpty() -> {
                    addCurrNumber(colIndex - 1)
                    result.add(Entity(rowIndex, char, colIndex, colIndex))
                }
            }
        }
        addCurrNumber(row.length - 1)
        return result
    }

    fun part1(input: List<String>): Int {
        val rows = input.flatMapIndexed { index: Int, row: String -> processRow(row, index) }
        val symbols = rows.filter { it -> !it.isNumber }.toSet()
        val numbers = rows.filter { it.isNumber }.toSet()
        val numbersWithNeighbours = numbers.filter { number -> symbols.any { number.isNeighbour(it) } }

        val result = numbersWithNeighbours.sumOf { it.number ?: 0 }
        println("""result: $result""")
        return result
    }

    fun part2(input: List<String>): Int {
        val rows = input.flatMapIndexed { index: Int, row: String -> processRow(row, index) }
        val result = rows.filter { it.raw == "*" }.sumOf {
            val neighbours = rows.filter { n -> n.isNumber && n.isNeighbour(it) }
            if (neighbours.size == 2) neighbours.first().number!! * neighbours.last().number!! else 0
        }
        println(result)
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
