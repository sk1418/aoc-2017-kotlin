import utils.*

// https://adventofcode.com/2017/day/24
fun main() {
    val today = "Day24"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parseToDay24(input: List<String>) = Day24(input.map { it.split("/") }.map { Component(it[0].toInt(), it[1].toInt()) }.toMutableSet())

    fun part1(input: List<String>): Int = parseToDay24(input).highestScore()
    fun part2(input: List<String>): Int = parseToDay24(input).highestScoreForLongestPath()

    chkTestInput(Part1, testInput, 31) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 19) { part2(it) }
    solve(Part2, input) { part2(it) }
}


private data class Component(val p1: Int, val p2: Int) {
    val score = p1 + p2
    infix fun canConnectTo(port: Int): Boolean = port == p1 || port == p2
    infix fun newPortAfterConnect(port: Int): Int = if (port == p1) p2 else p1
    fun canBeTheStartPoint() = 0 == p1 || 0 == p2
    override fun toString() = " $p1/$p2"
}

private data class NodeWithPath(val node: Component, val path: List<Component>, val openPort: Int)

private data class Day24(val components: MutableSet<Component>) {
    private val bridges: MutableList<List<Component>> = mutableListOf()
    private val starts = components.filter { it.canBeTheStartPoint() }
    private fun findAllBridges() {
        starts.forEach { start ->
            val stack = ArrayDeque<NodeWithPath>()
            stack.add(NodeWithPath(start, listOf(start), openPort = start.let { if (it.p1 == 0) it.p2 else it.p1 }))
            while (stack.isNotEmpty()) {
                val cur = stack.removeFirst()
                val possibleNexts = components.filter { it !in cur.path && it canConnectTo cur.openPort }
                if (possibleNexts.isEmpty()) {
                    bridges.add(cur.path)
                    continue
                }
                possibleNexts.forEach { next ->
                    val newOpenPort = next.newPortAfterConnect(cur.openPort)
                    stack.add(NodeWithPath(next, cur.path + next, newOpenPort))
                }
            }
        }
    }

    fun highestScore(): Int {
        findAllBridges()
        return bridges.maxOf { it.sumOf { c -> c.score } }
    }

    fun highestScoreForLongestPath(): Int {
        findAllBridges()
        val maxLen = bridges.maxOf { it.size }
        return bridges.groupBy { it.size }.toNotNullMap()[maxLen].maxOf { cList -> cList.sumOf { c -> c.score } }
    }
}