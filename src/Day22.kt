import utils.*
import utils.Direction.Up

// https://adventofcode.com/2017/day/22
fun main() {
    val today = "Day22"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toDay22Grid(input: List<String>): Day22Grid {
        val points = buildMap {
            input.forEachIndexed { y, row ->
                row.forEachIndexed { x, c -> put(x to y, c) }
            }
        }.toMutableMap()
        return Day22Grid(points, input.let { it.first().lastIndex / 2 to it.lastIndex / 2 })
    }

    fun part1(input: List<String>) = toDay22Grid(input).burst(10000)
    fun part2(input: List<String>) = toDay22Grid(input).burst2(10000000)

    chkTestInput(Part1, testInput, 5587) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2511944) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day22Grid(val points: MutableMap<Point, Char>, var cur: Point) {
    private var dir = Up
    var infected = 0

    fun burst(times: Int): Int {
        repeat(times) {
            val curChar = points.getOrPut(cur) { '.' }
            when (curChar) {
                //@formatter:off
                '.' -> { points[cur] = '#'; dir = dir.turn90Back(); infected++ }  //to left
                '#' -> { points[cur] = '.'; dir = dir.turn90() } //turn right
                //@formatter:on
            }
            cur = cur.move(dir)
        }
        return infected
    }

    fun burst2(times: Int): Int {
        repeat(times) {
            val curChar = points.getOrPut(cur) { '.' }
            when (curChar) {
                //@formatter:off
                '.' -> { points[cur] = 'w'; dir = dir.turn90Back() }
                'w' -> { points[cur] = '#'; infected++ }
                '#' -> { points[cur] = 'f'; dir = dir.turn90() }
                'f' -> { points[cur] = '.'; dir = dir.opposite() }
                //@formatter:on
            }
            cur = cur.move(dir)
        }
        return infected
    }
}