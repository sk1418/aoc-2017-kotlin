import utils.*

// https://adventofcode.com/2017/day/16
fun main() {
    val today = "Day16"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val programTest = "abcde".toList()
    val programReal = ('a'..'p').toList()

    fun part1(input: List<String>, program: List<Char>): String {
        return Day16(program, input.single().split(",").toList()).startDance()
    }

    fun part2(input: List<String>): String {
        return Day16(programReal, input.single().split(",").toList()).dance1bTimes()
    }

    chkTestInput(Part1, testInput, "baedc") { part1(it, programTest) }
    solve(Part1, input) { part1(it, programReal) }

    solve(Part2, input) { part2(it) }
}

private data class Day16(val programs: List<Char>, val actions: List<String>) {
    var charList = programs.toMutableList()
    private val swap: (Int) -> Unit = { charList = charList.rotateRight(it).toMutableList() }
    private val exchange: (Int, Int) -> Unit = { pos1, pos2 -> (charList[pos1] to charList[pos2]).apply { charList[pos1] = second; charList[pos2] = first } }
    private val partner: (Char, Char) -> Unit = { c1, c2 -> exchange(charList.indexOf(c1), charList.indexOf(c2)) }

    fun startDance(): String {
        actions.forEach { action ->
            when {
                action.startsWith("s") -> swap(action.drop(1).toInt())
                action.startsWith("x") -> action.drop(1).split("/").map { it.toInt() }.let { exchange(it[0], it[1]) }
                action.startsWith("p") -> partner(action[1], action[3])
            }
        }
        return charList.joinChars()
    }

    fun dance1bTimes(): String {
        val seen = mutableListOf(programs.joinChars())
        var i = 1
        while (i <= 1000000000) {
            val r = startDance()
            val idx = seen.indexOf(r)
            if (idx < 0) {
                seen.add(r)
                i++
            } else {
                val cycle = seen.lastIndex - idx + 1
                val leftRounds = 1000000000 - i
                val offset = leftRounds % cycle
                return seen[offset + idx]
            }
        }
        return seen.last()
    }
}