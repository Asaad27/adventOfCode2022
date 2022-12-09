package asaad

import java.io.File
import kotlin.math.abs

class DayNine(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file)
    private fun readInput(file: File) = file.bufferedReader().readLines()

    fun result() {
        println("\tpart1: ${solve(2)}")
        println("\tpart2: ${solve(10)}")
    }

    private fun solve(length: Int): Int{
        val board = Board(length)

        for (line in input) {
            val (direction, steps) = line.split(" ")
            when (direction) {
                "R" -> board.moveRight(steps.toInt())
                "L" -> board.moveLeft(steps.toInt())
                "D" -> board.moveDown(steps.toInt())
                "U" -> board.moveUp(steps.toInt())
                else -> throw Exception("Unknown direction $direction")
            }
        }

        return board.numberOfPositions()
    }

    class Board(length: Int) {

        private var rope = Array(length) {0 to 0}
        private val visitedPositions = HashSet<Pair<Int, Int>>()

        private fun distanceBetween(tail: Pair<Int, Int>, head: Pair<Int, Int>) = maxOf(
            abs(head.first - tail.first),
            abs(head.second - tail.second)
        )

        fun numberOfPositions() = visitedPositions.size

        private fun followNode(tail: Pair<Int, Int>, head: Pair<Int, Int>): Pair<Int, Int>{

            if (distanceBetween(head, tail) != 2)
                return tail

            val newX = when{
                head.first - tail.first > 0 -> tail.first + 1
                head.first - tail.first < 0 -> tail.first - 1
                else -> tail.first
            }

            val newY = when{
                head.second - tail.second > 0 -> tail.second + 1
                head.second - tail.second < 0 -> tail.second - 1
                else -> tail.second
            }

            return newX to newY
        }

        fun moveUp(steps: Int) {
            for (step in 1..steps) {
                rope[0] = rope[0].first to rope[0].second + 1
                follow()
                visitedPositions.add(rope.last())
            }
        }

        fun moveDown(steps: Int) {
            for (step in 1..steps) {
                rope[0] = rope[0].first to rope[0].second - 1
                follow()
                visitedPositions.add(rope.last())
            }
        }

        fun moveLeft(steps: Int) {
            for (step in 1..steps) {
                rope[0] = rope[0].first - 1 to rope[0].second
                follow()
                visitedPositions.add(rope.last())
            }
        }

        fun moveRight(steps: Int) {
            for (step in 1..steps) {
                rope[0] = rope[0].first + 1 to rope[0].second
                follow()
                visitedPositions.add(rope.last())
            }
        }

        private fun follow() {
            for (i in 1 until rope.size) {
                rope[i] = followNode(rope[i], rope[i - 1])
            }
        }
    }

}
