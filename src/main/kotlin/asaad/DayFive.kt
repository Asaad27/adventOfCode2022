package asaad

import java.io.File

class DayFive(filePath: String) {

    private val file = File(filePath)
    private val input = readInput(file)

    private fun readInput(file: File): Pair<List<String>, List<String>> {
        val lines = file.readLines()
        return lines.takeWhile { it.isNotBlank() } to lines.takeLastWhile { it.isNotBlank() }
    }

    fun result() {
        println("\tpart 1: ${solve(Stacks::applyMove)}")
        println("\tpart 2: ${solve(Stacks::applyMove2)}")
    }

    private fun solve(func: Stacks.(Move) -> Unit): String {
        val stacks = Stacks(input.first)
        for (line in input.second)
            stacks.func(Move(line))

        return stacks.getTops()
    }

    class Move(input: String){
        operator fun component1(): Int = x
        operator fun component2(): Int = from
        operator fun component3(): Int = to

        private val x: Int
        private val from : Int
        private val to: Int

        init {
            val split = input.split(" ")
            x = split[1].toInt()
            from = split[3].toInt()-1
            to = split[5].toInt()-1
        }
    }

    class Stacks(
        input: List<String>
    ){
        private val stacks :List<MutableList<String>>

        init {
            val stackNumbers = input.last().trim().split("\\s+".toRegex()).map { it.toInt() }
            val maxNumber = stackNumbers.max()
            stacks = List(maxNumber) { mutableListOf() }
            for (line in input.size-2 downTo 0){
                val split = input[line].chunked(4).map { it.trimEnd().removeSurrounding("[", "]") }
                for (number in split.indices){
                    if (split[number] == "")
                        continue
                    stacks[number].add(split[number])
                }
            }
        }

        fun applyMove(move: Move){
            val (x, from, to) = move
            repeat(x){
                stacks[to].add(stacks[from].last())
                stacks[from].removeLast()
            }
        }

        fun applyMove2(move: Move){
            val (x, from, to) = move

            stacks[to].addAll(stacks[from].takeLast(x))

            repeat(x){
                stacks[from].removeLast()
            }

        }

        fun getTops() = stacks.joinToString("") { it.last() }
    }
}


