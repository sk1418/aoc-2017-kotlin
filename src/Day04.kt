import utils.*

// https://adventofcode.com/2017/day/4
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int = input.count { it.split(" ").let { it.size == it.distinct().size } }

    fun part2(input: List<String>): Int =
        input.count {
            it.split(" ").map { w -> w.groupingBy { it }.eachCount() }.let { it.size == it.distinct().size }
        }

    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 3) { part2(it) }
    solve(Part2, input) { part2(it) }
}