package asaad

import java.io.File
import java.util.*

class DayOne(private val filePath: String) {

    private val file = File(filePath)
    private val input = readInput(file)
    fun result() {
        println("\tpart 1: ${solve(1, input)}")
        println("\tpart 2: ${solve(3, input)}")
    }

    private fun readInput(file: File) = file.readText()
        .split("\n\n", "\r\n\r\n")
        .map { group -> group.lines().sumOf { it.toIntOrNull() ?: 0 } }

    private fun solve(limit: Int, elseCalories: List<Int>): Int {
        val priorityQueue = PriorityQueue<Int>(Comparator.comparingInt { it })
        elseCalories.forEach { priorityQueue.addLimited(it, limit) }

        return priorityQueue.sum()
    }
}


fun <E> PriorityQueue<E>.addLimited(element: E, limit: Int) {
    add(element)
    if (size > limit) poll()
}

