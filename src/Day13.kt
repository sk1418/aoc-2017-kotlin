import utils.*

// https://adventofcode.com/2017/day/13
fun main() {
    val today = "Day13"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toPairs() = map { s -> s.split(": ").let { (layer, range) -> layer.toInt() to range.toInt() } }

    fun part1(input: List<String>) = Day13(input.toPairs()).severityAfterMovingThru()

    fun part2(input: List<String>) = Day13(input.toPairs()).unCaughtWithDelay()

    chkTestInput(Part1, testInput, 24) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 10) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day13(val layerRangePairs: List<Pair<Int, Int>>) {
    private val fwMap = layerRangePairs.toMap().toNotNullMap()

    //period when scanner moves back to 1 (very top)
    private val backTo1CycleMap = fwMap.map { (l, r) -> l to 2 * (r - 1) }.toMap().toNotNullMap()

    //when get caught: layer % backTo1Cycle ==0
    fun severityAfterMovingThru(): Int {
        return fwMap.entries.sumOf { (layer, range) ->
            if (layer % backTo1CycleMap[layer] == 0) layer * range else 0
        }
    }

    fun unCaughtWithDelay(): Int {
        var delay = 0
        while (fwMap.any { (layer, range) -> (layer + delay) % backTo1CycleMap[layer] == 0 }) {
            delay++
        }
        return delay
    }
}