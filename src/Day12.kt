class Row(val string: String, val pattern: List<Int>, val prefix: String) {
    fun isValidPrefix(): Boolean {
        val skipLast = string.contains('?')

        val strPattern = (prefix + string).substringBefore('?').split(".").filter { it.isNotEmpty() }

        return (skipLast || pattern.size == strPattern.size) && strPattern.foldIndexed(true) { index, acc, s ->
            acc && index < pattern.size && (if (skipLast) pattern[index] >= s.length else pattern[index] == s.length)
        }

    }

    fun nextRow(replacement: Char): Row? {
        val newString = string.replaceFirst('?', replacement)
        if (newString == string) {
            return null
        }
        val row = Row(string.substringAfter('?'), pattern, prefix + string.substringBefore('?') + replacement)
        return row
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Row> {
        return input.map {
            val splited = it.split(" ")
            Row(splited[0], splited[1].split(",").map { it.toInt() }, "")
        }
    }

    fun variantSequence(row: Row): Sequence<String> {
        return sequence {
            val questionCount = row.string.count { it == '?' }
            for (i in 0..<Math.pow(2.0, questionCount.toDouble()).toInt()) {
                val replacePattern = i.toString(2).padStart(questionCount, '0').replace('0', '.').replace('1', '#')
                var replaceIndex = 0;
                yield(row.string.mapIndexed { index, c ->
                    if (c == '?') {
                        val newC = replacePattern[replaceIndex]
                        replaceIndex++
                        newC
                    } else {
                        c
                    }
                }.joinToString(""))
            }
        }
    }

    fun variantSequenceRecursion(row: Row): Sequence<Row> {
        return sequence {
            if (row.isValidPrefix()) {
                val nextRowHash = row.nextRow('#')
                if (nextRowHash != null) {
                    variantSequenceRecursion(nextRowHash).forEach {
                        yield(it)
                    }
                }
                val nextRowDot = row.nextRow('.')
                if (nextRowDot != null) {
                    variantSequenceRecursion(nextRowDot).forEach {
                        yield(it)
                    }
                }
                if (!row.string.contains('?')) {
                    yield(row)
                }
            }

        }
    }


    fun part1(input: List<String>): Int {

        val rows = parseInput(input)
        val res = rows.sumOf { row ->
            val list = variantSequence(row).filter { pattern ->
                pattern.split(".").filter { s -> s.isNotEmpty() }.map { s ->
                    s.length
                }.joinToString(",") == row.pattern.joinToString(",")
            }.toList()

            list.size
        }
        println("""res of the part 1 is: $res""")
        return res
    }

    fun part1recursive(input: List<String>): Int {
        val rows = parseInput(input)

        val res = rows.sumOf {
            variantSequenceRecursion(it).count()
        }
        println("result is $res")
        return res
    }

    fun part2(input: List<String>): Long {

        var i = 0
        val res = input.sumOf {

            val splited = it.split(" ")
            val row =
                Row(splited[0].repeat(5), (splited[1] + ",").repeat(5).split(",").dropLast(1).map { it.toInt() }, "")
            println(
                """processing row $i of ${input.size},
                | row is ${row.string}, patternt: [${row.pattern.joinToString(", ")}]
            """.trimMargin()
            )
            i++
            variantSequenceRecursion(row).count().toLong()
        }
        println("""res of the part 2 is: $res""")
        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21)
    check(part1recursive(testInput) == 21)
//    check(part2(testInput) == 525152L)


    val input = readInput("Day12")
//    part1recursive(input).println()
//    part2(input).println()
}
