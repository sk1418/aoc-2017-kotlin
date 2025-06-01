import utils.*
import utils.Direction.*

// https://adventofcode.com/2017/day/14
fun main() {
    val today = "Day14"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day14(input.single()).sumSquares()
    fun part2(input: List<String>) = Day14(input.single()).findRegions()


    chkTestInput(Part1, testInput, 8108) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 1242) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day14(val input: String) {
    // reuse the knotHash() function from Day10
    fun knotHash(theStr: String) = Day10(255, theStr).knotHash()

    fun sumSquares(): Int {
        return (0..127).map { knotHash("$input-$it") }
            .sumOf { hash -> hash.sumOf { it.digitToInt(16).countOneBits() } }
    }

    // part2
    private val allOnePoints: MutableSet<Point> = mutableSetOf()
    private val chkQueue: ArrayDeque<Point> = ArrayDeque()
    private val visited: MutableSet<Point> = mutableSetOf()
    private var regionCounter = 0
    private val directions = listOf(Up, Left, Down, Right)

    private fun initGrid() {
        (0..127).map { knotHash("$input-$it") }
            .map { hash -> hash.flatMap { it.digitToInt(16).toString(2).padStart(4, '0').toList() } }
            .forEachIndexed { y, chars -> chars.forEachIndexed { x, char -> if (char == '1') allOnePoints.add(Point(x, y)) } }
    }

    fun findRegions(): Int {
        fun bfs() {
            while (chkQueue.isNotEmpty()) {
                val one = chkQueue.removeFirst()
                visited.add(one)

                directions.map { dir -> one.move(dir) }.intersect(allOnePoints)
                    .also { neighbourOnes ->
                        allOnePoints.removeAll(neighbourOnes)
                        chkQueue.addAll(neighbourOnes.filter { it !in visited })
                    }
            }
        }

        fun dfs() {
            while (chkQueue.isNotEmpty()) {
                val one = chkQueue.removeLast()
                visited.add(one)
                directions.map { dir -> one.move(dir) }.intersect(allOnePoints)
                    .also { neighbourOnes ->
                        allOnePoints.removeAll(neighbourOnes)
                        chkQueue.addAll(neighbourOnes.filter { it !in visited })
                    }
            }
        }

        initGrid()
        var theOne = allOnePoints.firstOrNull()
        while (theOne != null) {
            chkQueue.clear()
            regionCounter++
            chkQueue.add(theOne)
            allOnePoints.remove(theOne)
            bfs()
            theOne = allOnePoints.firstOrNull()
        }
        return regionCounter
    }
}