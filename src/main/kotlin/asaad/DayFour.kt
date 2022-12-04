package asaad

import java.io.File




class DayFour(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file).map { it.asRanges() }

    private fun readInput(file: File) = file.readLines()
    private val IntRange.size: Int
        get() {return this.last - this.first + 1}
    private fun IntRange.containsOneAnother(intRange: IntRange): Boolean {
        return this.intersect(intRange).size == minOf(intRange.size, this.size)
    }

    private fun IntRange.overlap(intRange: IntRange): Boolean {
        return this.intersect(intRange).isNotEmpty()
    }

    private fun String.asRanges(): List<IntRange> {
        return this.split(",").map { getFieldRange(it) }
    }

    private fun solve1() =
        input.count { it[0].containsOneAnother(it[1]) }

    private fun solve2() =
        input.count { it[0].overlap(it[1]) }

    fun result() {
        println("\tpart 1: ${solve1()}")
        println("\tpart 2: ${solve2()}")
    }

    /**
     * @param section: field section expression : "a-b"
     */
    private fun getFieldRange(section: String): IntRange {
        val (first, second) = section.split("-")
            .map { it.toInt() }
        return first..second
    }
}

