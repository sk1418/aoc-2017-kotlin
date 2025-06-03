import utils.*

// https://adventofcode.com/2017/day/15
fun main() {
    val today = "Day15"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parse(input: List<String>) =
        input.let { (aLine, bLine) -> aLine.substringAfterLast(" ").toInt() to bLine.substringAfterLast(" ").toInt() }

    fun part1(input: List<String>) =
        parse(input).let { (a, b) -> Day15(a, b, times = 40_000_000).countMatchesPart1() }

    fun part2(input: List<String>) =
        parse(input).let { (a, b) -> Day15(a, b, times = 5_000_000).countMatchesPart2() }

    chkTestInput(Part1, testInput, 588) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 309) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day15(val initA: Int, val initB: Int, val times: Int) {
    private val factorA = 16807
    private val factorB = 48271
    private val mask16bits: Long = 0xFFFF

    private fun generator(init: Int, factor: Int) = sequence {
        var v = init.toLong()
        while (true) {
            v = (v * factor) % 2147483647
            yield(v)
        }
    }

    fun countMatchesPart1(): Int {
        val seqA = generator(initA, factorA)
        val seqB = generator(initB, factorB)

        return seqA.zip(seqB).take(times).count { (a, b) ->
            a and mask16bits == b and mask16bits
        }
    }

    fun countMatchesPart2(): Int {
        val seqA = generator(initA, factorA)
        val seqB = generator(initB, factorB)

        return seqA.filter { it % 4 == 0L }.take(times)
            .zip(seqB.filter { it % 8 == 0L }.take(times))
            .count { (a, b) -> a and mask16bits == b and mask16bits }
    }

}