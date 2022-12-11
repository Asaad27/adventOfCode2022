package asaad

import java.io.File

class DayTen(filePath: String) {

    private val file = File(filePath)
    private val input = readInput(file)
    private fun readInput(file: File) = file.bufferedReader().readLines()

    fun result() {
        val answer = solve()
        println("\tpart1: ${answer.first}")
        println("\tpart2: \n${answer.second}")
    }

    private fun solve(): Pair<Int, String> {
        var cycle = 1
        var registry = 1
        var sum = 0
        val cycles = mutableListOf(20, 60, 100, 140, 180, 220)
        val drawing = Array(6) { Array(40) { '.' } }
        val spritePositions = intArrayOf(0, 1, 2)

        for (line in input) {
            val instruction = when {
                line.startsWith("noop") -> Instruction.NOOP to null
                line.startsWith("addx") -> Instruction.ADDX to line.split(" ")[1].toInt()
                else -> throw Exception("Unknown instruction $line")
            }

            repeat(instruction.first.cycles) {
                if (cycle in cycles)
                    sum += registry * cycle

                val currentPixelPosition = (cycle - 1).mod(40)
                val currentLine = (cycle - 1) / 40

                if (spritePositions.any { it == currentPixelPosition })
                    drawing[currentLine][currentPixelPosition] = '#'

                cycle++
            }

            if (instruction.second != null) {
                registry += instruction.second!!
                spritePositions[1] = registry
                spritePositions[0] = registry - 1
                spritePositions[2] = registry + 1
            }
        }

        return sum to drawing.joinToString("\n") { it.joinToString("") }
    }

    enum class Instruction(val cycles: Int) {
        NOOP(1),
        ADDX(2)
    }
}