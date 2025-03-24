import utils.*

// https://adventofcode.com/2017/day/9
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day09(input.single()).groupScore()

    fun part2(input: List<String>) = Day09(input.single()).countGarbage()

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 17) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day09(val input: String) {
    private fun String.removeGarbage() = this.replace("!.".toRegex(), "").replace("<[^>]*>".toRegex(), "")
    fun countGarbage(): Int {
        val re = "<([^>]*)>".toRegex()
        return re.findAll(input.replace("!.".toRegex(), "")).sumOf { it.groupValues[1].length }
    }

    fun groupScore(): Int {
        var score = 0
        var points = 0
        input.removeGarbage().replace(",", "").forEach {
            if (it == '{') {
                points++
            } else {
                score += (points--)
            }
        }
        return score
    }
}