package asaad

import java.io.File
import java.util.*


fun main() {
    val filePath = "sampleDayOne/input-day1.txt"

    println("part 1: ${solve(1, filePath)}")
    println("part 2: ${solve(3, filePath)}")

}


fun solve(limit: Int, filePath: String): Int{

    val file = File(filePath)
    val priorityQueue = PriorityQueue<Int>(Comparator.comparingInt { it })

    file.readText()
        .split("\n\n")
        .map { group -> group.lines().sumOf { it.toIntOrNull() ?: 0 } }
        .forEach { priorityQueue.addLimited(it, limit) }

    var sum = 0
    repeat(limit) {
        priorityQueue.apply { sum += this.poll() }
    }

    return sum
}

fun <E> PriorityQueue<E>.addLimited(element: E, limit: Int) {
    add(element)
    if (size > limit) poll()
}


