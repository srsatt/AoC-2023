class Hand(val raw: String, val bet: Long) {
    fun getRank(input: String): String {
        val chars = input.toList()
        val charSet = chars.toSet()
        // five of a kind
        if (charSet.size == 1) {
            return "7"
        }
        // high card
        if (charSet.size == 5) {
            return "1"
        }
        //one pair
        if (charSet.size == 4) {
            return "2"
        }

        // four or full-house
        if (charSet.size == 2) {
            val sorted = chars.sorted()
            return if (sorted[1] == sorted[3]) "6" else "5"
        }
        //two pairs or three
        // AA ABC 7; AB BBC 6;  AB CCC  4
        // AA BBC 5; AB BCC 6; AA BCC 5
        val sorted = chars.sorted()
        val head = sorted.slice(0..1).toSet()
        val tail = sorted.slice(2..4).toSet()
        val p = head.size + 2 * tail.size
        if (p == 5) {
            return "3"
        }
        if (p == 7 || p == 4) {
            return "4"
        }
        return if (sorted.slice(2..4)[0] == sorted.slice(2..4)[1]) "4" else "3"
    }


    val cmpValue: Long
        get() {
            val tail = raw.toList().map {
                when (it) {
                    'T' -> 'A'
                    'J' -> 'B'
                    'Q' -> 'C'
                    'K' -> 'D'
                    'A' -> 'E'
                    else -> it
                }
            }.joinToString("")
            return (getRank(raw) + tail).toLong(16)
        }
    val cmpValueWithJoker: Long
        get() {
            val tail = raw.toList().map {
                when (it) {
                    'T' -> 'A'
                    'J' -> '1'
                    'Q' -> 'C'
                    'K' -> 'D'
                    'A' -> 'E'
                    else -> it
                }
            }.joinToString("")

            val jokersCount = raw.toList().filter { it == 'J' }.size
            val cardMap = mutableMapOf<Char, Int>()
            if (raw == "JJJJJ") {
                return (getRank("AAAAA") + tail).toLong(16)
            }
            for (card in raw.toList().filter { it != 'J' }) {
                cardMap[card] = cardMap.getOrDefault(card, 0) + 1
            }
            val popularCard = cardMap.entries.maxWith { a, b -> a.value - b.value }
            cardMap[popularCard.key] = cardMap[popularCard.key]!! + jokersCount
            val bestHand = cardMap.entries.joinToString("") { it.key.toString().repeat(it.value) }
            return (getRank(bestHand) + tail).toLong(16)
        }

}

fun main() {

    fun parseRow(row: String): Hand {
        val list = row.split(" ")
        return Hand(list[0], list[1].toLong())
    }

    fun part1(input: List<String>): Long {
        return input.map { parseRow(it) }.sortedBy {
            it.cmpValue
        }
            .foldIndexed(0L) { index: Int, acc: Long, hand: Hand ->
                acc + (index.toLong() + 1L) * hand.bet
            }
    }

    fun part2(input: List<String>): Long {
        return input.map { parseRow(it) }.sortedBy {
            it.cmpValueWithJoker
        }
            .foldIndexed(0L) { index: Int, acc: Long, hand: Hand ->
                acc + (index.toLong() + 1L) * hand.bet
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
