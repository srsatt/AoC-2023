class Cell(val row: Int, val col: Int, val content: String)
enum class Direction { up, down, left, right }


class Field(val cells: List<List<Cell>>) {
    val dirMap = mapOf(
        Direction.down to setOf("J", "L", "|", "S"),
        Direction.up to setOf("7", "F", "|", "S"),
        Direction.left to setOf("L", "F", "-", "S"),
        Direction.right to setOf("J", "7", "-", "S")
    )

    fun get(row: Int, col: Int): Cell? {
        return cells.getOrNull(row)?.getOrNull(col)
    }


    fun nextCell(prev: Pair<Cell?, Direction>): Pair<Cell?, Direction>? {
        val (cell, from) = prev
        if (cell == null) {
            return null
        }

        return when (from) {
            Direction.up -> {
                when (cell.content) {
                    "|" -> Pair(get(cell.row + 1, cell.col), Direction.up)
                    "L" -> Pair(get(cell.row, cell.col + 1), Direction.left)
                    "J" -> Pair(get(cell.row, cell.col - 1), Direction.right)
                    else -> null
                }
            }

            Direction.down -> {
                when (cell.content) {
                    "|" -> Pair(get(cell.row - 1, cell.col), Direction.down)
                    "F" -> Pair(get(cell.row, cell.col + 1), Direction.left)
                    "7" -> Pair(get(cell.row, cell.col - 1), Direction.right)
                    else -> null
                }
            }

            Direction.right -> {
                when (cell.content) {
                    "-" -> Pair(get(cell.row, cell.col - 1), Direction.right)
                    "F" -> Pair(get(cell.row + 1, cell.col), Direction.up)
                    "L" -> Pair(get(cell.row - 1, cell.col), Direction.down)
                    else -> null
                }
            }

            Direction.left -> {
                when (cell.content) {
                    "-" -> Pair(get(cell.row, cell.col + 1), Direction.left)
                    "7" -> Pair(get(cell.row + 1, cell.col), Direction.up)
                    "J" -> Pair(get(cell.row - 1, cell.col), Direction.down)
                    else -> null
                }
            }
        }
    }
}

fun main() {
    fun getField(input: List<String>): Field {
        return Field(input.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, content ->
                Cell(rowIndex, colIndex, content.toString())
            }
        })
    }

    fun getStart(field: Field): List<Pair<Cell?, Direction>> {
        val start = field.cells.flatten().find { it.content == "S" }!!
        val l = mutableListOf<Pair<Cell?, Direction>>()
        val left = field.get(row = start.row, col = start.col - 1)
        if (field.dirMap[Direction.left]!!.contains(left?.content ?: "")) {
            l.add(Pair(left, Direction.right))
        }
        val right = field.get(row = start.row, col = start.col + 1)
        if (field.dirMap[Direction.right]!!.contains(right?.content ?: "")) {
            l.add(Pair(right, Direction.left))
        }
        val up = field.get(row = start.row - 1, col = start.col)
        if (field.dirMap[Direction.up]!!.contains(up?.content ?: "")) {
            l.add(Pair(up, Direction.down))
        }
        val down = field.get(row = start.row + 1, col = start.col)
        if (field.dirMap[Direction.down]!!.contains(down?.content ?: "")) {
            l.add(Pair(down, Direction.up))
        }
        return l.toList()

    }

    fun iterateLoop(field: Field): Pair<Set<Cell>, Int> {
        val start = field.cells.flatten().find { it.content == "S" }!!
        var tick = 0

        var curr = getStart(field)

        val visited = mutableSetOf(start)
        visited.addAll(curr.mapNotNull { it.first })
        while (curr.isNotEmpty()) {
            curr = curr.mapNotNull { field.nextCell(it) }.filter { c -> !visited.contains(c.first) }
            visited.addAll(curr.mapNotNull { it.first })
            tick++
        }
        return Pair(visited.toSet(), tick)
    }

    fun part1(input: List<String>): Int {
        val field = getField(input)
        val (_, tick) = iterateLoop(field)
        return tick

    }

    fun part2(input: List<String>): Int {
        val field = getField(input)
        val (visited) = iterateLoop(field)
        val result = field.cells.flatten().filter {
            val content = it.content
            fun rayCast(ray: IntRange, pos: Int, dir: Direction, cell: Cell): Boolean {
                val border = setOf("FJ", "JF", "L7", "7L")


                val isHorizontal = dir == Direction.left || dir == Direction.right

                var rayCells = ray.map { f ->
                    if (isHorizontal) field.get(row = pos, col = f) else field.get(row = f, col = pos)
                }.filter { it != null && visited.contains(it) }

                if (dir == Direction.up || dir == Direction.left) {
                    rayCells = rayCells.reversed()
                }


                val splits = mutableListOf(mutableListOf<Cell>())
                if (rayCells.isNotEmpty()) {
                    splits.last().add(rayCells.first()!!)
                }

                for (next in rayCells.drop(1)) {
                    val split = splits.last()
                    if (next == null) {
                        continue
                    }
                    if (field.dirMap[dir]!!.contains(next.content)) {
                        split.add(next)
                    } else {
                        splits.add(mutableListOf(next))
                    }
                }
//                if (rayCells.isNotEmpty() && rayCells.last() != null) {
//                    splits.add(mutableListOf(rayCells.last()!!))
//                }


                val res = splits.filter { split ->
                    split.size > 0 && (split.size == 1 || border.contains(
                        split.first().content + split.last().content
                    ))
                }.size % 2 == 1


                return res

            }

            val res = !visited.contains(it) && rayCast(it.row + 1..<field.cells.size, it.col, Direction.down, it) &&
                    rayCast(0..<it.row, it.col, Direction.up, it) &&
                    rayCast(it.col + 1..<field.cells[0].size, it.row, Direction.right, it) &&
                    rayCast(0..<it.col, it.row, Direction.left, it)

            res
        }

        return result.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val testInput2 = readInput("Day10-2_test")
    check(part1(testInput) == 8)
    check(part2(testInput2) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
