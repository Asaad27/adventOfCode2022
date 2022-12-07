package asaad

import java.io.File

class DaySeven(filePath: String) {
    private val file = File(filePath)
    private val input = readInput(file)
    private var lineNumber = 0

    private fun readInput(file: File) = file.bufferedReader().readLines()

    fun result() {
        val head = Node(name = "head")
        val root = Node(name = "/")
        head.children.add(root)
        createFileTree(head)

        println("\tpart 1: ${solve1(root)}")
        println("\tpart 2: ${solve2(root)}")
    }

    private fun solve1(root: Node) = sumOfDirsWithAtMost(100000, root)

    private fun solve2(root: Node): Int {
        val maxSize = 70000000
        val required = 30000000
        val used = root.sum
        val available = maxSize - used
        if (available >= required)
            return 0

        val sizes = mutableListOf<Int>()
        getDirsSizes(root, sizes)
        sizes.sort()


        var answer = 0
        var (left, right) = listOf(0, sizes.size - 1)
        while (left <= right) {
            val mid = left + (right - left) / 2
            val current = sizes[mid]
            if (available >= required - current) {
                answer = current
                right = mid - 1
            } else {
                left = mid + 1
            }
        }

        return answer
    }

    /**
     * @param sizes: the list where the directory sizes will be appended
     * gets the list of directory sizes
     */
    private fun getDirsSizes(node: Node, sizes: MutableList<Int>) {

        sizes.add(node.sum)
        for (child in node.children)
            getDirsSizes(child, sizes)

    }

    private fun sumOfDirsWithAtMost(size: Int, node: Node): Int {

        var sum = 0
        if (node.sum <= size && node.name != "/") {
            sum += node.sum
        }

        for (child in node.children) {
            sum += sumOfDirsWithAtMost(size, child)
        }

        return sum
    }

    /**
     * Creates a dir tree, and computes the size of each directory
     */
    private fun createFileTree(current: Node): Int {
        var nextLine = input.readNext()
        val nodes = HashMap<String, Node>()
        while (nextLine != null) {
            if (nextLine.startsWith("$")) {
                val command = parseCommand(nextLine)
                when (command.first) {
                    Command.LS -> {
                        nextLine = input.readNext()
                        continue
                    }

                    Command.GOTO -> {
                        val dirNode = command.second!!
                        if (dirNode == "/")
                            nodes[dirNode] = current.children.first()
                        current.sum += createFileTree(nodes[dirNode]!!)
                    }

                    Command.BACK -> return current.sum
                }
            } else if (nextLine.startsWith("dir")) {
                val dirName = nextLine.split(" ")[1]
                val dirNode = Node(name = dirName)
                nodes[dirName] = dirNode
                current.children.add(dirNode)
            } else if (nextLine[0].isDigit()) {
                val fileSize = nextLine.split(" ")[0].toInt()
                current.sum += fileSize
            } else {
                throw Exception("unable to parse line $nextLine")
            }
            nextLine = input.readNext()
        }

        return current.sum
    }

    private fun parseCommand(line: String): Pair<Command, String?> {
        val split = line.split(" ")
        return when {
            split.size > 1 && split[1] == "ls" -> Command.LS to null
            split.size > 2 && split[1] == "cd" && split[2] == ".." -> Command.BACK to null
            split.size > 2 && split[1] == "cd" -> Command.GOTO to split[2]
            else -> throw Exception("unknown command $line")
        }
    }

    enum class Command {
        GOTO,
        BACK,
        LS
    }

    data class Node(
        var name: String,
        var sum: Int = 0,
        var children: MutableList<Node> = mutableListOf(),

        )

    private fun <E> List<E>.readNext(): String? {
        if (lineNumber > size - 1)
            return null

        return input[lineNumber].also { lineNumber++ }
    }

}


