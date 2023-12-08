class Node(val value: String, var left: Node?, var right: Node?)

class Graph(val pattern: String, val nodeList: List<Node>)

fun main() {

    fun parseInput(input: List<String>): Graph {

        val pattern = input[0]
        val map = mutableMapOf<String, Node>()
        val regex = """([0-9a-zA-Z]+) = \(([0-9a-zA-Z]+), ([0-9a-zA-Z]+)\)""".toRegex()
        var start: Node? = null
        var list = mutableListOf<Node>()

        input.drop(2).forEach {
            val matchResult = regex.matchEntire(it)
            val (value, left, right) = matchResult!!.destructured
            val getOrPutNode = { v: String ->
                map.getOrPut(v) {
                    val node = Node(v, null, null)
                    list.add(node)
                    node
                }
            }
            val curr = getOrPutNode(value)
            curr.left = getOrPutNode(left)
            curr.right = getOrPutNode(right)

        }
        return Graph(pattern, list.toList())
    }

    fun part1(input: List<String>): Int {
        val graph = parseInput(input)
        var i = 0
        var curr = graph.nodeList.find { it.value == "AAA" }!!
        while (true) {
            if (curr.value == "ZZZ") {
                break
            }
            val next = graph.pattern[i % graph.pattern.length]
            i += 1;
            curr = if (next == 'R') curr.right!! else curr.left!!
        }
        return i

    }

    fun part2(input: List<String>): Long {
        val graph = parseInput(input)
        var curr = graph.nodeList.filter { it.value.endsWith('A') }

        class Visit(val tick: Int, val count: Int, val node: Node)
        class Cycle(val head: Int, val length: Int, val zTicks: List<Int>)

        val cycles = curr.map {

            val zTicks = mutableListOf<Int>()
            var count = 0
            var node = it
            val visited = mutableSetOf<Visit>()

            var visit: Visit? = null
            while (true) {
                visit = visited.find { v -> v.node.value == node.value && v.tick == (count % graph.pattern.length) }
                if (visit != null) {
                    break
                }
                visited.add(Visit(count % graph.pattern.length, count, node))

                val next = graph.pattern[(count % graph.pattern.length).toInt()]
                node = if (next == 'R') node.right!! else node.left!!
                if (node.value.endsWith('Z')) {
                    zTicks.add(count)
                }
                count += 1
            }
            val head = count - visit!!.count
            val length = count - visit!!.count
            Cycle(head, length, zTicks.toList().map { it })
        }


        return cycles.fold(graph.pattern.length.toLong()) { acc, cycle -> acc * cycle.length / graph.pattern.length.toLong() }


    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val testInput2 = readInput("Day08-2_test")
    check(part1(testInput) == 6)
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
