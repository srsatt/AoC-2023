class Record(val string: String, val pattern: List<Int>)


fun recordIsValid(record: Record): Boolean {
    val lengthEnough = record.pattern.sum() + record.pattern.size <= record.string.length
    val blocks = record.string.split(*listOf("?", ".").toTypedArray())
        .filter { s -> s.isNotBlank() }.map { it.length }
    val blocksIsEnough = blocks.size <= record.pattern.size + record.pattern.sum()
    val maxPattern = (blocks.maxOrNull() ?: 0) <= (record.pattern.maxOrNull() ?: 0)

    return lengthEnough && blocksIsEnough && maxPattern
}


fun interateVariants(record: Record, map: MutableMap<String, Long> = mutableMapOf<String, Long>()): Long {
    if (map.contains(record.string + record.pattern.joinToString(","))) {
        return map[record.string + record.pattern.joinToString(",")]!!
    }
    var res = 0L;
    if (!record.string.contains('#') && record.pattern.isEmpty()) {
//            yield(Record(record.string.replace('?', '.'), listOf()))
        res = 1L;
        map.put(record.string + record.pattern.joinToString(","), res)
        return res
    }
    val maxBlockLength = record.pattern.maxOrNull()
    if (maxBlockLength != null) {
        val variantsCount = record.string.windowedSequence(maxBlockLength).mapIndexed { index, window ->
            if (!window.contains('.') && (index == 0 || record.string.get(index - 1) != '#')) {
                val indexOfMax = record.pattern.indexOf(maxBlockLength)
                var head = record.string.substring(0..<index)

                var tail = record.string.substring(index + window.length)



                if (
                    (tail.isEmpty() || tail.first() != '#') && (head.isEmpty() || head.last() != '#')
                ) {
                    if (head.lastOrNull() == '?') {
                        head = head.removeSuffix("?") + '.'
                    }
                    if (tail.firstOrNull() == '?') {
                        tail = '.' + tail.removePrefix("?")
                    }
                    val headRecord = Record(head, record.pattern.slice(0..<indexOfMax))
                    val tailRecord = Record(tail, record.pattern.drop(indexOfMax + 1))
                    if (recordIsValid(headRecord) && recordIsValid(tailRecord)) {
                        interateVariants(tailRecord, map) * interateVariants(headRecord, map)
//                            .forEach { tRecord ->
//                                .forEach { hRecord ->
//                                    yield(
//                                        Record(
//                                            hRecord.string + "#".repeat(maxBlockLength) + tRecord.string,
//                                            listOf<Int>()
//                                        )
//                                    )
//                                }
//                            }
//                        }
                    } else {
                        0
                    }
                } else {
                    0L
                }
            } else {
                0L
            }
        }.sum()
        res = variantsCount
        map.put(record.string + record.pattern.joinToString(","), res)
        return res
    }
    res = 0L
    map.put(record.string + record.pattern.joinToString(","), res)
    return res
}

fun main() {


    fun part2(input: List<String>, repeatCount: Int): Long {

        val records = input.map {
            val splited = it.split(" ")

            Record(
                (0..<repeatCount).map { splited[0] }.joinToString("?"),
                (splited[1] + ",").repeat(repeatCount).split(",").dropLast(1).map { it.toInt() }
            )
        }

        val res = records.sumOf {
            println(
                """parsing string: ${it.string} [${it.pattern.joinToString(", ")}]""".trimIndent()
            )

            val res = interateVariants(it)
            println("""result for string is: $res""")
            res
        }
        println("""res of the part 2 is: $res""")
        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")

    check(part2(listOf("????.#...#... 4,1,1"), 1) == 1L)

    check(part2(testInput, 1) == 21L)

    check(part2(testInput, 5) == 525152L)


    val input = readInput("Day12")
    part2(input, 5).println()
}
