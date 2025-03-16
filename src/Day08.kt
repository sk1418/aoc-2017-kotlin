import utils.*

// https://adventofcode.com/2017/day/8
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day08(input).theMaxWhenComplete()

    fun part2(input: List<String>) = Day08(input).theMaxInExecution()

    chkTestInput(Part1, testInput, 1) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 10) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private operator fun <E> List<E>.component6(): E = this[5]
private operator fun <E> List<E>.component7(): E = this[6]
private data class Day08(val input: List<String>) {
    private var theMaxWhenExecuting = 0
    private val varMap = mutableMapOf<String, Int>()

    private fun executeSingle(parts: List<String>) {
        val (v1Name, op, n1, theIf, v2Name, condition, n2) = parts
        val v2 = varMap.getOrPut(v2Name) { 0 }
        val num2 = n2.toInt()
        val goAhead = when (condition) {
            "==" -> v2 == num2
            ">=" -> v2 >= num2
            ">" -> v2 > num2
            "<" -> v2 < num2
            "<=" -> v2 <= num2
            "!=" -> v2 != num2
            else -> error("Unknown condition $condition")
        }

        val updateMax = { newV: Int -> theMaxWhenExecuting = maxOf(theMaxWhenExecuting, newV) }
        if (goAhead) {
            val v1 = varMap.getOrPut(v1Name) { 0 }
            when (op) {
                "inc" -> varMap[v1Name] = (v1 + n1.toInt()).also { updateMax(it) }
                "dec" -> varMap[v1Name] = (v1 - n1.toInt()).also { updateMax(it) }
            }
        }
    }

    fun theMaxWhenComplete(): Int {
        input.forEach { executeSingle(it.split(" ")) }
        return varMap.values.max()
    }

    fun theMaxInExecution(): Int {
        input.forEach { executeSingle(it.split(" ")) }
        return theMaxWhenExecuting
    }
}