import utils.*

// https://adventofcode.com/2017/day/5
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun getDay05(input: List<String>) = Day05(input.map { it.toInt() }.toMutableList())

    fun part1(input: List<String>) = getDay05(input).jump { curOffset: Int -> curOffset + 1 }
    fun part2(input: List<String>) = getDay05(input).jump { curOffset: Int -> curOffset + if (curOffset >= 3) -1 else 1 }


    chkTestInput(Part1, testInput, 5) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 10) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day05(val jumpSeq: MutableList<Int>) {
    var curIdx = 0
    var steps = 0
    fun jump(incrementOffset: (Int) -> Int): Int {
        while (curIdx in jumpSeq.indices) {
            val offset = jumpSeq[curIdx]
            jumpSeq[curIdx] = incrementOffset(offset)
            curIdx += offset
            steps++
        }
        return steps
    }
}