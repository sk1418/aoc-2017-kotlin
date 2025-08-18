import utils.*

// https://adventofcode.com/2017/day/18
fun main() {
    val today = "Day18"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun parseInput(input: List<String>): List<Instruction> =
        input.map { line ->
            val parts = line.split(" ")
            val op = parts[0]
            val x = parts.getOrNull(1) ?: ""
            val y = parts.getOrNull(2) ?: ""
            Instruction(Triple(op, x, y))
        }

    fun part1(input: List<String>): Long {
        return Day18Part1(parseInput(input)).firstRcvValue()
    }

    fun part2(input: List<String>): Int {
        val instructions = parseInput(input)
        return Day18Part2(Program(instructions, 0L), Program(instructions, 1L)).start()
    }

    chkTestInput(Part1, testInput, 4L) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

@JvmInline
private value class Instruction(val triple: Triple<String, String, String>) {
    val op get() = triple.first
    val x get() = triple.second
    val y get() = triple.third
    operator fun component1() = op
    operator fun component2() = x
    operator fun component3() = y
}

private data class Day18Part1(val instructions: List<Instruction>) {
    private var cur = 0
    private val varMap: MutableMap<String, Long> = mutableMapOf()
    private fun readValue(s: String): Long = s.toLongOrNull() ?: varMap.getOrPut(s) { 0L }
    fun firstRcvValue(): Long {
        while (cur < instructions.size) {
            val (op, x, y) = instructions[cur]
            when (op) {
                "set" -> varMap[x] = readValue(y)
                "snd" -> varMap["snd"] = readValue(x)
                "add" -> varMap[x] = readValue(x) + readValue(y)
                "mul" -> varMap[x] = readValue(x) * readValue(y)
                "mod" -> varMap[x] = readValue(x) % readValue(y)
                "rcv" if (readValue(x) != 0L) -> return varMap["snd"] ?: 0L
                "jgz" if (readValue(x) > 0) -> {
                    cur += readValue(y).toInt(); continue
                }
            }
            cur++

        }
        error("'rcv' never hit")
    }
}

// part2
private data class Program(val instructions: List<Instruction>, val programId: Long) {
    var cur = 0
    val queue = ArrayDeque<Long>()
    val varMap: MutableMap<String, Long> = mutableMapOf("p" to programId)
    fun readValue(s: String): Long = s.toLongOrNull() ?: varMap.getOrPut(s) { 0L }
    var sendCount = 0
}

private data class Day18Part2(val prog0: Program, val prog1: Program) {
    private fun step(me: Program, other: Program): Boolean {
        val (op, x, y) = me.instructions[me.cur]
        when (op) {
            "set" -> me.varMap[x] = me.readValue(y)
            "add" -> me.varMap[x] = me.readValue(x) + me.readValue(y)
            "mul" -> me.varMap[x] = me.readValue(x) * me.readValue(y)
            "mod" -> me.varMap[x] = me.readValue(x) % me.readValue(y)
            "snd" -> other.queue.addLast(me.readValue(x)).also { if (me.programId == 1L) me.sendCount++ }
            "rcv" -> {
                me.queue.removeFirstOrNull()?.let { me.varMap[x] = it } ?: return false
            }

            "jgz" -> if (me.readValue(x) > 0) {
                me.cur += (me.readValue(y).toInt() - 1)
            }
        }
        me.cur++
        return true
    }

    fun start(): Int {
        while (true) {
            val initEmpty0 = prog0.queue.isEmpty()
            val initEmpty1 = prog1.queue.isEmpty()
            val stepP0 = step(prog0, prog1)
            val stepP1 = step(prog1, prog0)
            if (prog0.cur >= prog0.instructions.size && prog1.cur >= prog1.instructions.size) break
            if (!stepP0 && !stepP1 && initEmpty0 && initEmpty1 && prog0.queue.isEmpty() && prog1.queue.isEmpty()) break
        }
        return prog1.sendCount
    }
}