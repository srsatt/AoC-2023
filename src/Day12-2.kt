class Record(val string: String, val pattern: List<Int>)

fun interateVariants(record: Record): Sequence<Record> {
    return sequence {
        if (!record.string.contains('#') && record.pattern.isEmpty()) {
            yield(Record(record.string.replace('?', '.'), listOf()))
        }
        val firstBlockLength = record.pattern.firstOrNull()
        if (firstBlockLength != null) {

            record.string.windowedSequence(firstBlockLength).forEachIndexed { index, window ->
                if (!window.contains('.') && (index == 0 || record.string.get(index - 1) != '#')) {
                    val prefix = record.string.substring(0..<index).replace('?', '.') + window.replace(
                        '?',
                        '#'
                    )

                    val tail = record.string.substring(index + window.length)

                    if (
                        (tail.isEmpty() || tail.first() != '#') && tail.replace(".", "").split('?')
                            .filter { s -> s.isNotBlank() }.size <= record.pattern.size + record.pattern.sum()
                    ) {
                        val tailRecord = Record(tail.replaceFirstChar { '.' }, record.pattern.drop(1))
                        interateVariants(tailRecord).forEach {
                            yield(Record(prefix + it.string, listOf<Int>()))
                        }
                    }
                }
            }
        }
    }
}

fun main() {


    fun part2(input: List<String>, repeatCount: Int): Long {

        val records = input.map {
            val splited = it.split(" ")
            val s = (0..repeatCount).map { splited[0] }.joinToString("?")

            Record(
                (0..<repeatCount).map { splited[0] }.joinToString("?"),
                (splited[1] + ",").repeat(repeatCount).split(",").dropLast(1).map { it.toInt() }
            )
        }

        val res = records.sumOf {
            println(
                """parsing string: ${it.string} [${it.pattern.joinToString(", ")}]""".trimIndent()
            )
            val res = interateVariants(it).count().toLong()
            println("""result for string is: $res""")
            res
        }
        println("""res of the part 2 is: $res""")
        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")

    check(part2(testInput, 1) == 21L)

    check(part2(testInput, 5) == 525152L)


    val input = readInput("Day12")
    part2(input, 5).println()
}
