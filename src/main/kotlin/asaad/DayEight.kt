package asaad

import java.io.File
import java.util.*

class DayEight(filePath: String) {

    private val file = File(filePath)
    private val input = readInput(file)
    private fun readInput(file: File) = file.bufferedReader().readLines()


    fun result() {
        val solution = solve()
        println("\tpart 1: ${solution.first}")
        println("\tpart 2: ${solution.second}")
    }

    private fun solve(): Pair<Int, Int> {

        val numberOfRows = input.size
        if (numberOfRows == 0)
            throw Exception("Empty file")

        val numberOfCols = input[0].length

        val trees = HashSet<Tree>()

        val topToBottomDec = Array(numberOfCols) { Stack<Tree>() }
        val leftToRightDec = Stack<Tree>()

        for (rowNumber in 0 until  numberOfRows) {
            for (colNumber in 0 until numberOfCols) {

                val height = input[rowNumber][colNumber].digitToInt()
                val currentNode = Tree(
                    height = height,
                    rowNumber = rowNumber,
                    colNumber = colNumber,
                    clearRight = numberOfCols-1-colNumber,
                    clearLeft = colNumber,
                    clearTop = rowNumber,
                    clearBottom = numberOfRows-1-rowNumber
                ).also { trees.add(it) }

                if (leftToRightDec.isEmpty())
                    currentNode.hasViewOnEdge = true
                var foundEqualHeightLtoR = false
                while (leftToRightDec.isNotEmpty() && leftToRightDec.peek().height <= currentNode.height) {
                    val popped = leftToRightDec.pop().apply {
                        clearRight = colNumber - this.colNumber
                    }

                    if (currentNode.height == popped.height){
                        currentNode.clearLeft = colNumber - popped.colNumber
                        foundEqualHeightLtoR = true
                    }

                    if (leftToRightDec.empty() && currentNode.height != popped.height) currentNode.hasViewOnEdge = true
                }
                if (leftToRightDec.isNotEmpty() && !foundEqualHeightLtoR){
                    currentNode.apply {
                        clearLeft = colNumber - leftToRightDec.peek().colNumber - 1
                    }
                }
                leftToRightDec.push(currentNode)


                if (topToBottomDec[colNumber].isEmpty())
                    currentNode.hasViewOnEdge = true
                var foundEqualHeightTtoB = false
                while (topToBottomDec[colNumber].isNotEmpty() && topToBottomDec[colNumber].peek().height <= currentNode.height) {
                    val popped = topToBottomDec[colNumber].pop().apply {
                        clearBottom = rowNumber - this.rowNumber
                    }
                    if (currentNode.height == popped.height){
                        currentNode.clearTop = rowNumber - popped.rowNumber
                        foundEqualHeightTtoB = true
                    }

                    if (topToBottomDec[colNumber].empty() && currentNode.height != popped.height){
                            currentNode.hasViewOnEdge = true
                    }

                }
                if (topToBottomDec[colNumber].isNotEmpty() && !foundEqualHeightTtoB){
                    currentNode.apply {
                        clearTop = rowNumber - topToBottomDec[colNumber].peek().rowNumber - 1
                    }
                }
                topToBottomDec[colNumber].push(currentNode)
            }

            while (leftToRightDec.isNotEmpty()) {
                leftToRightDec.pop().apply {
                    hasViewOnEdge = true
                    clearRight = (numberOfCols-1) - this.colNumber
                }
            }

            leftToRightDec.clear()
        }


        for (stack in topToBottomDec) {
            while (stack.isNotEmpty()){
                stack.pop().apply {
                    hasViewOnEdge = true
                    clearBottom = (numberOfRows-1) - this.rowNumber
                }
            }
        }

        return trees.count { it.hasViewOnEdge } to trees.maxOf { it.getScenicScore() }
    }

    data class Tree(
        private val id: Int = genId(),
        var height: Int,
        var rowNumber: Int = 0,
        var colNumber: Int = 0,
        var clearRight: Int = 0,
        var clearTop: Int = 0,
        var clearBottom: Int = 0,
        var clearLeft: Int = 0,
        var hasViewOnEdge: Boolean = false
    ) {
        fun getScenicScore() = clearRight * clearBottom * clearTop * clearLeft

        companion object IdGenerator {
            var id = 0
            fun genId() = id.also { id++ }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Tree

            return id == other.id
        }

        override fun hashCode() = id

    }
}