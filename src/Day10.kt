import utils.*

// https://adventofcode.com/2017/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testInputPart2 = readTestInput("$today-part2")

    fun part1(maxNum: Int, input: List<String>) = Day10(maxNum, input.single()).firstTwoProduct()

    fun part2(maxNum: Int, input: List<String>) = Day10(maxNum, input.single()).knotHash()

    chkTestInput(Part1, testInput, 12) { part1(4, it) }
    solve(Part1, input) { part1(255, it) }

    chkTestInput(Part2, testInputPart2, "3efbe78a8d82f29979031a4aa0b16a9d") { part2(255, it) }
    solve(Part2, input) { part2(255, it) }
}

private data class Day10(val maxElement: Int, val lengthStr: String) {

    val theList: MutableList<Int> = (0..maxElement).toMutableList()
    var cur = 0
    var skip = 0

    fun firstTwoProduct(): Int {
        lengthStr.toInts(",").forEach { param -> step(param) }
        return theList[0] * theList[1]
    }

    fun knotHash(): String {
        val lenParamPart2 = lengthStr.map { it.code } + listOf(17, 31, 73, 47, 23)
        repeat(64) {
            lenParamPart2.forEach { param -> step(param) }
        }
        return theList.chunked(16).map { l16 ->
            l16.reduce { a, b -> a xor b }.toString(16).padStart(2, '0')
        }.joinToString("")
    }


    fun step(lenParam: Int) {
        reverseBy(lenParam)
        cur = (cur + (skip + lenParam)) % theList.size
        skip++
    }

    private fun reverseBy(lenParam: Int) {
        val toIdx = cur + lenParam - 1
        if (toIdx <= theList.lastIndex) {
            val sub = theList.subList(cur, toIdx + 1)
            val rev = sub.reversed()
            sub.apply { clear(); addAll(rev) }

        } else {
            val rearSub = theList.subList(cur, theList.size)
            val headSub = theList.subList(0, lenParam - rearSub.size)
            val reversed = ArrayDeque((rearSub + headSub).reversed())
            (0..rearSub.lastIndex).forEach {
                rearSub[it] = reversed.removeFirst()
            }
            (0..headSub.lastIndex).forEach {
                headSub[it] = reversed.removeFirst()
            }
        }
    }


}