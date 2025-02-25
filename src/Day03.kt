import utils.*
import utils.Direction.*
import kotlin.math.absoluteValue

// https://adventofcode.com/2017/day/3
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val part2TestInput = readTestInput("$today-part2")

    fun part1(input: List<String>): Int {
        return Day03().distance(input.single().toInt())
    }

    fun part2(input: List<String>): Int {
        return Day03().walkInSpiral(input.single().toInt())
    }

    chkTestInput(Part1, testInput, 31) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, part2TestInput, 351) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day03 {
    val layers = mutableListOf<Int>()
    private fun calcLayers(n: Int) {
        var layer = 1
        while ((layer * layer) < n) {
            layers.add(layer)
            layer += 2
        }
        layers.add(layer)
    }

    fun distance(n: Int): Int {
        calcLayers(n)
        val myIdx = layers.lastIndex //same steps to the 1st layer
        val leastMiddle = layers[myIdx - 1].let { it * it } + 1 + (myIdx - 1) //right side middle point
        val disToMiddle = (0..3).map { it * myIdx * 2 + leastMiddle }.minOf { (it - n).absoluteValue }
        return disToMiddle + myIdx
    }


    fun walkInSpiral(limit: Int): Int {
        val pointMap = mapOf(Point(0, 0) to 1).toMutableNotNullMap()
        var curPoint = Point(0, 0)
        var stepsToTurn = 2 //each layer needs 2 more steps to turn (reaches the corner)
        val directions = listOf(Up, Left, Down, Right)

        val calValue: (Point) -> Int = { point: Point -> point.allAdjacents().filter { it in pointMap }.sumOf { pointMap[it] } }

        while (true) {
            curPoint = curPoint.move(Right)
            pointMap[curPoint] = calValue(curPoint).also { if (it > limit) return it }
            (0..3).forEach {//4 directions
                val dir = directions[it]
                repeat(if (dir == Up) stepsToTurn - 1 else stepsToTurn) {
                    curPoint = curPoint.move(dir)
                    pointMap[curPoint] = calValue(curPoint).also { if (it > limit) return it }
                }
            }
            stepsToTurn += 2
        }
    }


}