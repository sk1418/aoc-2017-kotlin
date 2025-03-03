import utils.*

// https://adventofcode.com/2017/day/6
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = MemoryReallocation(input.single().toInts().toMutableList()).part1()
    fun part2(input: List<String>) = MemoryReallocation(input.single().toInts().toMutableList()).part2()

    chkTestInput(Part1, testInput, 5) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 4) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class MemoryReallocation(val banks: MutableList<Int>) {
    val results = mutableListOf(banks.toList())
    fun reDistribute(): Int {
        while (true) {
            val (selIdx, blocks) = banks.withIndex().maxBy { it.value }
            banks[selIdx] = 0
            var (allAdd, remBlocks) = blocks.let { it / banks.size to it % banks.size }
            val requiredIdxes = buildList {
                var theIdx = if (selIdx == banks.lastIndex) 0 else selIdx + 1
                while (remBlocks > 0) {
                    add(theIdx)
                    theIdx = (theIdx + 1) % banks.size
                    remBlocks--
                }
            }
            banks.indices.forEach { banks[it] = (banks[it] + allAdd + if (it in requiredIdxes) 1 else 0) }
            val seenCycle = results.indexOf(banks)
            if (seenCycle > 0) return seenCycle
            results += banks.toList()
        }
    }

    fun part1() = reDistribute().let { results.size }
    fun part2() = reDistribute().let { results.size - it }
}