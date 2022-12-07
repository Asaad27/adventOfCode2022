package asaad

import java.io.File

private const val MAX_STORAGE = 70000000
private const val REQUIRED_STORAGE = 30000000
private const val PART1 = 100000

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
        val dirSizes = mutableListOf<Int>()
        getDirsSizes(root, sizes = dirSizes)
        dirSizes.sort()

        println("\tpart 1: ${solve1(dirSizes)}")
        println("\tpart 2: ${solve2(root, dirSizes)}")
    }

    /**
     * Time complexity: O(Log(N)) where N is the number of directories
     */
    private fun solve2(root: Node, sizes: MutableList<Int>): Int {
        val used = root.dirSize
        val available = MAX_STORAGE - used

        if (available >= REQUIRED_STORAGE)
            return 0

        var answer = 0
        var (left, right) = listOf(0, sizes.size - 1)
        while (left <= right) {
            val mid = left + (right - left) / 2
            val current = sizes[mid]
            if (available >= REQUIRED_STORAGE - current) {
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
     * Space Complexity : O(N) where N is the number of directories
     */
    private fun getDirsSizes(node: Node, sizes: MutableList<Int>) {
        sizes.add(node.dirSize)
        for (child in node.children)
            getDirsSizes(child, sizes)
    }

    /**
     * Time Complexity: O(N) where N is the number of directories
     */
    private fun solve1(dirSizes: List<Int>) = dirSizes.takeWhile { it <= PART1 }.sum()

    /**
     * Creates a dir tree, and computes the size of each directory
     * Time Complexity: O(M) where M is the number of lines
     */
    private fun createFileTree(current: Node): Int {
        var nextLine = input.readNext()
        val nodes = HashMap<String, Node>()
        while (nextLine != null) {
            when {
                nextLine.startsWith("$") -> {
                    val command = parseCommand(nextLine)
                    when (command.first) {
                        Command.LS -> {}

                        Command.GOTO -> {
                            val dirNode = command.second!!
                            if (dirNode == "/")
                                nodes[dirNode] = current.children.first()
                            current.dirSize += createFileTree(nodes[dirNode]!!)
                        }

                        Command.BACK -> return current.dirSize
                    }
                }

                nextLine.startsWith("dir") -> {
                    val dirName = nextLine.split(" ")[1]
                    val dirNode = Node(name = dirName)
                    nodes[dirName] = dirNode
                    current.children.add(dirNode)
                }

                nextLine[0].isDigit() -> {
                    val fileSize = nextLine.split(" ")[0].toInt()
                    current.dirSize += fileSize
                }

                else -> {
                    throw Exception("unable to parse line $nextLine")
                }
            }

            nextLine = input.readNext()
        }

        return current.dirSize
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
        var dirSize: Int = 0,
        var children: MutableList<Node> = mutableListOf(),
    )

    private fun <E> List<E>.readNext(): String? {
        if (lineNumber > size - 1)
            return null

        return input[lineNumber].also { lineNumber++ }
    }
}


