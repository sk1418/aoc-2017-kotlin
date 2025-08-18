import utils.*

// https://adventofcode.com/2017/day/19
fun main() {
    val today = "Day19"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toDay19Matrix(input: List<String>): Day19Matrix {
        val maxX = input.maxOf { it.length } - 1
        val maxY = input.size - 1
        val points = buildMap {
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    put(x to y, ' ')
                }
            }
        }.toMutableNotNullMap()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c -> if (c != ' ') points[x to y] = c }
        }
        return Day19Matrix(maxX, maxY, points)
    }

    fun part1(input: List<String>) = toDay19Matrix(input).traverse().first
    fun part2(input: List<String>) = toDay19Matrix(input).traverse().second

    chkTestInput(Part1, testInput, "ABCDEF") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 38) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day19Matrix(maxX: Int, maxY: Int, override val points: MutableNotNullMap<Point, Char>) : Matrix<Char>(maxX, maxY, points) {
    private var cur = points.keys.first { (x, y) -> y == 0 && points[x to y] == '|' }
    private var dir = Direction.Down
    private val stops = mutableListOf<Char>()
    private var steps = 0

    private fun Point.isValidOne() = validAndExist() && points[this] != ' '

    fun traverse(): Pair<String, Int> {
        while (true) {
            cur = cur.move(dir)
            steps++
            if (cur !in points || points[cur] == ' ') return stops.joinChars() to steps
            when (val c = points[cur]) {
                in 'A'..'Z' -> stops.add(c)
                '+' -> dir = listOf(dir.turn90(), dir.turn90Back()).first { cur.move(it).isValidOne() }
            }
        }
    }
}