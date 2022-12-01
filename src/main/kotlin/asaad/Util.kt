package asaad

object Util {
     fun readStr() = readln() // string line
     fun readInt() = readStr().toInt() // single int
     fun readStrings() = readStr().split(" ") // list of strings
     fun readInts() = readStrings().map { it.toInt() } // list of ints
}