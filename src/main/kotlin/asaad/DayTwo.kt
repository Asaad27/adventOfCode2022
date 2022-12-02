package asaad

import java.io.File

class DayTwo(filePath: String) {
    /*
     *  rock -> Scissors -> paper ->rock
     */
    private var debug = false
    private var part1 = true
    private val hands = listOf(
        "Rock" to 1,
        "Paper" to 2,
        "Scissors" to 3
    )

    private val handMapper = mapOf(
        "A" to "Rock",
        "X" to "Rock",
        "Y" to "Paper",
        "B" to "Paper",
        "Z" to "Scissors",
        "C" to "Scissors"
    )

    private val outcomeMapper = mapOf(
        "X" to OUTCOME.LOOSE,
        "Y" to OUTCOME.DRAW,
        "Z" to OUTCOME.WIN
    )

    private val file = File(filePath)
    private val input = readInput(file)

    private fun solve() =
        input.fold(0) { acc: Int, round: String ->
            acc + roundScore(round).also { if (debug) println("score : $it") }
        }

    fun result() {
        println("\tpart 1: ${solve()}")
        part1 = false
        println("=====================")
        println("\tpart 2: ${solve()}")
    }

    private fun roundScore(round: String): Int {
        var score = 0
        val (opponent, secondCol) = round.split(" ")
            .mapIndexed { index, s -> if ((index == 0) || part1) handParser(s) else s }
        val mine = if (part1) secondCol else outcomeToHand(opponent, outcomeMapper[secondCol]!!)
        score += hands.first { it.first == mine }.second
        score += roundOutcome(opponent, mine).weight

        return score
    }

    private fun getIndex(element: String) = hands.map { it.first }.indexOf(element)

    private fun roundOutcome(opponent: String, mine: String): OUTCOME = when {
        opponent == mine -> OUTCOME.DRAW
        (getIndex(opponent) + 1).mod(hands.size)
                == getIndex(mine) -> OUTCOME.WIN

        else -> OUTCOME.LOOSE
    }.also { if (debug) println("$opponent vs $mine : $it") }

    private fun outcomeToHand(opponent: String, outcome: OUTCOME): String = when (outcome) {
        OUTCOME.LOOSE -> hands[(getIndex(opponent) + hands.size - 1).mod(hands.size)].first
        OUTCOME.WIN -> hands[(getIndex(opponent) + 1).mod(hands.size)].first
        else -> opponent
    }

    private fun handParser(hand: String) = handMapper[hand]!!

    private fun readInput(file: File) = file.readLines()

    private enum class OUTCOME(val weight: Int) {
        WIN(6),
        LOOSE(0),
        DRAW(3);
    }
}

