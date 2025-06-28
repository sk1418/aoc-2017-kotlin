import utils.*

// https://adventofcode.com/2017/day/17
fun main() {
    val today = "Day17"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return Day17(input.single().toInt()).startInsertion()
    }

    fun part2(input: List<String>): Int {
        return Day17(input.single().toInt()).insert50M()
    }

    chkTestInput(Part1, testInput, 638) { part1(it) }
    solve(Part1, input) { part1(it) }

    solve(Part2, input) { part2(it) }
}

private data class Day17(val steps: Int) {
    val buffer = mutableListOf(0)
    var currentIdx = 0
    var numAtIdx1 = -1
    private fun insert(n: Int, onlyCareIdx1:Boolean = false) {
        val possiblePos = currentIdx + steps
        if(onlyCareIdx1 ) {
            currentIdx = possiblePos % n + 1 // n= buffer.size if all num inserted
            if(currentIdx==1) numAtIdx1 = n
        }else {
            currentIdx = possiblePos % buffer.size + 1
            buffer.add(currentIdx, n)
        }
    }

    fun startInsertion(): Int {
        (1..2017).forEach { insert(it) }
        return buffer[currentIdx + 1]
    }
    fun insert50M(): Int {// the number after 0 is always index=1, no need to insert all numbers
        (1..50000000).forEach { insert(it, onlyCareIdx1 = true) }
        return numAtIdx1
    }
}