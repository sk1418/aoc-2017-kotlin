import utils.*

// https://adventofcode.com/2017/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day07(input).buildTower().name

    fun part2(input: List<String>) = Day07(input).findTheExpectedWeight()

    chkTestInput(Part1, testInput, "tknk") { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 60) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Node(val name: String, val weight: Int) {
    val children: MutableList<Node> = mutableListOf()
    var parent: Node? = null
    var sum = weight
}

private class Day07(val input: List<String>) {
    private val splitRe = """( [(]|[)])""".toRegex()
    private val splitNodeRe = """( [(]|[)] -> |, )""".toRegex()

    fun buildTower(): Node {
        val allNodes = buildList {
            input.forEach { l -> l.split(splitRe).let { add(Node(it[0], it[1].toInt())) } }
        }
        input.filter { "->" in it }.map { line ->
            val branch = line.split(splitNodeRe)
            val fatherNode = allNodes.first { it.name == branch.first() }
            fatherNode.children += allNodes.filter { it.name in branch.drop(1) }
            fatherNode.children.forEach { child -> child.parent = fatherNode }
        }
        return allNodes.first { it.parent == null }
    }

    fun findTheExpectedWeight(): Int {
        val root = buildTower().also { it.sumIt() }
        root.findUnbalancedValue()
        return thePair.let { (n, target) -> n.weight + (target - n.sum) }
    }

    private fun Node.sumIt() {
        if (children.isNotEmpty()) sum += children.sumOf { it.sumIt(); it.sum }
    }

    private lateinit var thePair: Pair<Node, Int>
    private fun Node.findUnbalancedValue() {
        val m = children.groupBy { it.sum }
        if (m.size == 1) return
        check(m.size == 2)
        val theNode = m.entries.first { it.value.size == 1 }.value.first()
        thePair = theNode to m.keys.first { it != theNode.sum }
        children.forEach { it.findUnbalancedValue() }
    }

}