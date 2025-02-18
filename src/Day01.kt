import utils.*

// https://adventofcode.com/2017/day/1
fun main() {
    val today = "Day01"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testInputPart2 = readTestInput("$today-part2")

    fun part1(input: List<String>): Int {
        val digits = input.single().toCharArray()
        return digits.mapIndexed { i, n ->
            val nextIdx = if (i == digits.lastIndex) 0 else i + 1
            if (n == digits[nextIdx]) n.digitToInt() else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val digits = input.single().toCharArray()
        val steps = digits.size / 2
        val dd = digits + digits
        return digits.mapIndexed { i, n -> if (n == dd[i + steps]) n.digitToInt() else 0 }.sum()
    }

    chkTestInput(Part1, testInput, 4) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInputPart2, 4) { part2(it) }
    solve(Part2, input) { part2(it) }
}