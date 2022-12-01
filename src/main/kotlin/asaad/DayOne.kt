package asaad

import java.io.File
import java.util.*

fun main() {
    val filePath = "sampleDayOne/input-day1.txt"
    val file = File(filePath)
    val input = readInput(file)

    println("part 1: ${solve(1, input)}")
    println("part 2: ${solve(3, input)}")
}
fun readInput(file: File) = file.readText()
    .split("\n\n", "\r\n\r\n")
    .map { group -> group.lines().sumOf { it.toIntOrNull()?:0 }}
fun solve(limit: Int, elseCalories: List<Int>): Int{
    val priorityQueue = PriorityQueue<Int>(Comparator.comparingInt { it })

    elseCalories.forEach { priorityQueue.addLimited(it, limit) }

    return priorityQueue.sum()
}

fun <E> PriorityQueue<E>.addLimited(element: E, limit: Int) {
    add(element)
    if (size > limit) poll()
}


