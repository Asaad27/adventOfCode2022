package asaad

import java.io.File

class DaySix(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file)

    private fun readInput(file: File) = file.readLines().first()

    fun result() {
        println("\tpart 1: ${solve(4)}")
        println("\tpart 2: ${solve(14)}")
    }

    private fun solve(size: Int): Int {
        val map = HashMap<Char, Int>()
        var (left, length) = listOf(0, input.length)

        for (right in 0 until length) {
            val current = input[right]
            map[current] = (map[current] ?: 0) + 1

            while (map[current]!! > 1) {
                val currentLeft = input[left]
                map[currentLeft] = map[currentLeft]!! - 1
                left++
            }

            if (right - left + 1 == size)
                return right + 1
        }

        throw Exception("did not find an answer for size $size")
    }
}