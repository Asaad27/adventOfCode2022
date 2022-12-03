package asaad

import java.io.File

class DayTwo(filePath: String) {
    /*
     *  rock -> Scissors -> paper ->rock
     */
    private var debug = false
    private var part1 = true

    private val handMapper = mapOf(
        "A" to HAND.ROCK,
        "X" to HAND.ROCK,
        "Y" to HAND.PAPER,
        "B" to HAND.PAPER,
        "Z" to HAND.SCISSORS,
        "C" to HAND.SCISSORS
    )

    private val outcomeMapper = mapOf(
        "X" to OUTCOME.LOOSE,
        "Y" to OUTCOME.DRAW,
        "Z" to OUTCOME.WIN
    )

    private val file = File(filePath)
    private val input = readInput(file)

    private val handSize = HAND.values().size

    private fun solve() =
        input.fold(0) { acc: Int, round: String ->
            acc + roundScore(round).also { if (debug) println("score : $it") }
        }

    fun result() {
        println("\tpart 1: ${solve()}")
        part1 = false
        println("\tpart 2: ${solve()}")
    }

    private fun roundScore(round: String): Int {
        var score = 0
        val (firstCol, secondCol) = round.split(" ")
        val opponent = handParser(firstCol)

        val mine = if (part1) handParser(secondCol) else outcomeToHand(opponent, outcomeMapper[secondCol]!!)
        score += mine.weight
        score += roundOutcome(opponent, mine).weight

        return score
    }

    private fun roundOutcome(opponent: HAND, mine: HAND): OUTCOME = when {
        opponent == mine -> OUTCOME.DRAW
        (opponent.ordinal + 1).mod(handSize)
                == mine.ordinal -> OUTCOME.WIN

        else -> OUTCOME.LOOSE
    }.also { if (debug) println("$opponent vs $mine : $it") }

    private fun outcomeToHand(opponent: HAND, outcome: OUTCOME): HAND = when (outcome) {
        OUTCOME.LOOSE -> HAND.values()[(opponent.ordinal + handSize - 1).mod(handSize)]
        OUTCOME.WIN -> HAND.values()[(opponent.ordinal + 1).mod(handSize)]
        else -> opponent
    }

    private fun handParser(input: String) = handMapper[input]!!

    private fun readInput(file: File) = file.readLines()

    private enum class OUTCOME(val weight: Int) {
        WIN(6),
        LOOSE(0),
        DRAW(3);
    }

    private enum class HAND(val weight: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);
    }
}

