import utils.*

// https://adventofcode.com/2017/day/2
fun main() {
    val today = "Day02"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = input.map { line -> line.toInts() }.sumOf {
        it.max() - it.min()
    }

    fun part2(input: List<String>) = input.map { line -> line.toInts() }.sumOf { numbers->
        numbers.flatMap { a -> numbers.mapNotNull { b -> if (a > b && a % b == 0) a / b else null } }.first()

    }

    chkTestInput(Part1, testInput, 18) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}