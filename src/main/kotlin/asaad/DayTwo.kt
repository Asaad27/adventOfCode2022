package asaad

import java.io.File

class DayTwo(filePath: String) {
    /*
     *  rock -> Scissors -> paper ->rock...  ->win, <-loose
     */
    private val file = File(filePath)
    private val input = readInput(file)

    private fun readInput(file: File) = file.readLines()

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

    private val handSize = HAND.values().size

    fun result() {
        println("\tpart 1: ${solve()}")
        part1 = false
        println("\tpart 2: ${solve()}")
    }

    private fun solve() =
        input.fold(0) { acc: Int, round: String ->
            acc + roundScore(round)
        }

    private fun roundScore(round: String): Int {
        var score = 0
        val (firstCol, secondCol) = round.split(" ")
        val opponentHand = handParser(firstCol)

        val myHand = if (part1) handParser(secondCol) else outcomeToHand(opponentHand, outcomeParser(secondCol))
        score += myHand.score + roundOutcome(opponentHand, myHand).score

        return score
    }

    private fun roundOutcome(opponent: HAND, mine: HAND): OUTCOME = when {
        opponent == mine -> OUTCOME.DRAW
        (opponent.ordinal + 1).mod(handSize)
                == mine.ordinal -> OUTCOME.WIN

        else -> OUTCOME.LOOSE
    }

    private fun outcomeToHand(opponent: HAND, outcome: OUTCOME): HAND = when (outcome) {
        OUTCOME.LOOSE -> HAND.values()[(opponent.ordinal + handSize - 1).mod(handSize)]
        OUTCOME.WIN -> HAND.values()[(opponent.ordinal + 1).mod(handSize)]
        else -> opponent
    }

    private fun handParser(input: String) = handMapper[input]!!

    private fun outcomeParser(input: String) = outcomeMapper[input]!!

    private enum class OUTCOME(val score: Int) {
        WIN(6),
        LOOSE(0),
        DRAW(3);
    }

    private enum class HAND(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);
    }
}

