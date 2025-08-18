import utils.*

// https://adventofcode.com/2017/day/21
fun main() {
    val today = "Day21"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return fractalArt(input, 5)
    }

    fun part2(input: List<String>): Int {
        return fractalArt(input, 18)
    }

    chkTestInput(Part1, testInput, 12) {fractalArt(testInput,2) }
    solve(Part1, input) { part1(it) }

//    chkTestInput(Part2, testInput, 0) { part2(it) }
    solve(Part2, input) { part2(it) }
}


private fun List<String>.rotate(): List<String> {
    return indices.map { rowIdx -> reversed().map { it[rowIdx] }.joinToString("") }
}

private fun List<String>.flip(): List<String> = map { it.reversed() }

private fun List<String>.allPossibilities() = generateSequence(this) { it.rotate() }.take(4).flatMap { listOf(it, it.flip()) }.toSet()
private fun List<String>.breakIntoBlocks(): List<List<String>> {
    val size = size
    val blockSize = if (size % 2 == 0) 2 else 3
    return chunked(blockSize).flatMap { rows ->
        (0 until size step blockSize).map { col ->
            rows.map { it.substring(col, col + blockSize) }
        }
    }
}

private fun List<List<String>>.reassemble(newBlockSize: Int, blocksPerRow: Int): List<String> {
    val rows = mutableListOf<String>()
    for (y in 0 until size step blocksPerRow) {
        for (i in 0 until newBlockSize) {
            rows.add(
                subList(y, y + blocksPerRow)
                    .joinToString("") { it[i] })
        }
    }
    return rows
}

private fun List<String>.enhance(
    rules: Map<List<String>, List<String>>,
): List<String> {
    val blocks = breakIntoBlocks()
    val newBlocks = blocks.map { block ->
        rules[block] ?: error("No rule for block: $block")
    }
    val newBlockSize = newBlocks.first().size
    val blocksPerRow = (size / blocks.first().size)
    return newBlocks.reassemble(newBlockSize, blocksPerRow)
}

private fun fractalArt(
    rulesInput: List<String>,
    iterations: Int,
): Int {
    val rules = rulesInput.flatMap { line ->
        val (from, to) = line.split(" => ")
        val pattern = from.split("/")
        val output = to.split("/")
        pattern.allPossibilities().map { it to output }
    }.toMap()

    var grid = listOf(".#.", "..#", "###")
    repeat(iterations) {
        grid = grid.enhance(rules)
    }
    return grid.sumOf { row -> row.count { it == '#' } }
}