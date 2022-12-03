package asaad

import java.io.File

class DayThree(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file)

    private fun readInput(file: File) = file.readLines()

    private fun Char.toPriority(): Int = when {
        this.isLowerCase() -> this - 'a' + 1
        this.isUpperCase() -> this - 'A' + 27
        else -> throw Exception("char $this has no priority")
    }

    private fun solve1() =
        input.fold(0) { acc, s -> acc + RuckSack(s).repeatedItem().toPriority() }

    private fun solve2() =
        input.chunked(3){ chunk ->
            chunk.map { it.toSet() }
                .reduce { acc, chars -> acc.intersect(chars) }
                .firstOrNull()
        }.fold(0) { acc, s -> acc + s?.toPriority()!! }

    fun result() {
        println("\tpart 1: ${solve1()}")
        println("\tpart 2: ${solve2()}")
    }

    private class RuckSack(ruckSack: String) {

        private val compartments: List<Set<Char>>

        init {
            val ruckSize = ruckSack.length
            compartments = listOf(
                ruckSack.substring(0 until ruckSize / 2).toHashSet(),
                ruckSack.substring(ruckSize / 2 until ruckSize).toHashSet()
            )
        }

        fun repeatedItem(): Char {
            val intersection = compartments[0].intersect(compartments[1].toSet())
            if (intersection.size > 1)
                throw Exception("there is more than one repeated element in the compartments")

            return intersection.first()
        }

    }
}
