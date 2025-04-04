import utils.*
import kotlin.math.abs
import kotlin.math.max

// https://adventofcode.com/2017/day/11
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day11(input.single().split(",")).distanceAfterMove()
    fun part2(input: List<String>) = Day11(input.single().split(",")).furthestDistance()


    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

/**
 *	x-axis: ne and sw (/)
 *	y-axis: n and s (|)
 *	z-axis: se and nw (\)
 *  `x+y+z == 0`
 */
private data class Pos(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    infix fun moveInDirection(dir: String) = apply {
        // @formatter:off
        when (dir) {
            "n" -> {y++; z--}
            "s" -> {y--; z++}
            "ne" -> {x++; y--}
            "sw" -> {x--; y++}
            "nw" -> {z++; x--}
            "se" -> {z--; x++}
        }
        // @formatter:on
    }

    fun distanceFromStart() = listOf(abs(x), abs(y), abs(z)).max()
}

private data class Day11(val moves: List<String>) {
    fun distanceAfterMove(): Int {
        val pos = Pos()
        moves.forEach { m ->
            pos moveInDirection m
        }
        return pos.distanceFromStart()
    }

    fun furthestDistance(): Int {
        val pos = Pos()
        var furthestDistance = 0
        moves.forEach { m ->
            pos moveInDirection m
            furthestDistance = max(furthestDistance, pos.distanceFromStart())
        }
        return furthestDistance
    }
}