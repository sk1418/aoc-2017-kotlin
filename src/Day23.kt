import utils.Part1
import utils.readInput
import utils.readTestInput
import utils.solve

// https://adventofcode.com/2017/day/23
fun main() {
    val today = "Day23"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parseInput(input: List<String>): List<Program23> =
        input.map { line ->
            val parts = line.split(" ")
            val op = parts[0]
            val x = parts.getOrNull(1) ?: ""
            val y = parts.getOrNull(2) ?: ""
            Program23(Triple(op, x, y))
        }

    fun part1(input: List<String>) = Day23(parseInput(input)).mulCounter()
    //part2 is calculated by reverse engineering

    solve(Part1, input) { part1(it) }
}

@JvmInline
private value class Program23(val triple: Triple<String, String, String>) {
    val op get() = triple.first
    val x get() = triple.second
    val y get() = triple.third
    operator fun component1() = op
    operator fun component2() = x
    operator fun component3() = y
}

private data class Day23(val program23s: List<Program23>, val initialA: Long = 0L) {
    private var cur = 0
    private val varMap: MutableMap<String, Long> = mutableMapOf()
    private fun readValue(s: String): Long = s.toLongOrNull() ?: varMap.getOrPut(s) { if (s == "a") initialA else 0L }
    fun mulCounter(): Int {
        var mulCount = 0
        while (cur < program23s.size) {
            val (op, x, y) = program23s[cur]
            when (op) {
                "set" -> varMap[x] = readValue(y)
                "sub" -> varMap[x] = readValue(x) - readValue(y)
                "mul" -> {
                    varMap[x] = readValue(x) * readValue(y); mulCount++
                }

                "jnz" if (readValue(x) != 0L) -> {
                    cur += readValue(y).toInt(); continue
                }
            }
            cur++
        }
        return mulCount
    }
}