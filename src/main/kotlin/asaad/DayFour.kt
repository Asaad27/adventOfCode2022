package asaad

import java.io.File

private fun IntRange.containsOneAnother(intRange: IntRange): Boolean {
    return this.intersect(intRange).size == minOf(intRange.size, this.size)
}

private fun IntRange.overlap(intRange: IntRange): Boolean {
    return this.intersect(intRange).isNotEmpty()
}

private val IntRange.size: Int
    get() {
        return this.last - this.first + 1
    }

class DayFour(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file).map { it.asRanges() }

    private fun readInput(file: File) = file.readLines()

    private fun String.asRanges(): List<IntRange> {
        return this.split(",").map { getFieldRange(it) }
    }

    private fun solve(func: IntRange.(IntRange) -> Boolean) =
        input.count { it[0].func(it[1]) }

    fun result() {
        println("\tpart 1: ${solve(IntRange::containsOneAnother)}")
        println("\tpart 2: ${solve(IntRange::overlap)}")
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

