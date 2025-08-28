import utils.*

// https://adventofcode.com/2017/day/25
fun main() {
    val today = "Day25"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int = parseInput(input).run()

    fun part2(input: List<String>): Int {
        return 0
    }

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 0) { part2(it) }
    solve(Part2, input) { part2(it) }
}
data class Rule(val write: Int, val move: Int, val nextState: String)

private fun parseInput(lines: List<String>): Day25 {
    val startState = Regex("Begin in state (\\w)").find(lines[0])!!.groupValues[1]
    val steps = Regex("Perform a diagnostic checksum after (\\d+) steps.")
        .find(lines[1])!!.groupValues[1].toInt()

    val rules = mutableMapOf<Pair<String, Int>, Rule>()
    var i = 3 // skip the first 2 lines + blank

    while (i < lines.size) {
        val state = Regex("In state (\\w):").find(lines[i])!!.groupValues[1]
        i += 1

        repeat(2) {
            val curVal = Regex("If the current value is (\\d):")
                .find(lines[i++])!!.groupValues[1].toInt()

            val write = Regex("- Write the value (\\d).")
                .find(lines[i++])!!.groupValues[1].toInt()

            val moveStr = Regex("- Move one slot to the (left|right).")
                .find(lines[i++])!!.groupValues[1]
            val move = if (moveStr == "right") 1 else -1

            val nextState = Regex("- Continue with state (\\w).")
                .find(lines[i++])!!.groupValues[1]

            rules[state to curVal] = Rule(write, move, nextState)
        }
        i++ // skip blank line
    }

    return Day25(steps, startState, rules)
}

private data class Day25(val steps: Int, val startState: String, val rules: Map<Pair<String, Int>, Rule>) {
    fun run(): Int {
        val tape = mutableSetOf<Int>()
        var pos = 0
        var state = startState
        repeat(steps) {
            val curValue = if (pos in tape) 1 else 0
            val rule = rules[state to curValue] ?: error("No rule for $state,$curValue")

            // write
            if (rule.write == 1) tape.add(pos) else tape.remove(pos)
            // move
            pos += rule.move
            // next state
            state = rule.nextState
        }

        return tape.size // checksum
    }
}