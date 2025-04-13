import utils.*

// https://adventofcode.com/2017/day/12
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toMap() = map { it.split(" <-> ").let { (k, v) -> k.toInt() to v.toInts(", ") } }.toMap().toNotNullMap()
    fun part1(input: List<String>) = Day12(input.toMap()).findZeroRelated()

    fun part2(input: List<String>) = Day12(input.toMap()).findGroups()

    chkTestInput(Part1, testInput, 6) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day12(val map: NotNullMap<Int, List<Int>>) {
    val zeroRelated = mutableSetOf(0)
    private fun findThem(key: Int) {
        val newValues = map[key].toSet() - zeroRelated
        zeroRelated += newValues
        newValues.forEach { k ->
            findThem(k)
        }
    }

    fun findZeroRelated(): Int {
        findThem(0)
        return zeroRelated.size
    }

    //part2
    val programs = map.toMutableNotNullMap()
    private val groups = mutableMapOf<Int, MutableSet<Int>>().toMutableNotNullMap()
    fun findGroups(): Int {
        while (programs.isNotEmpty()) {
            val key = programs.keys.first()
            groups[key] = mutableSetOf()
            findByKey(key, key)
            programs.remove(key)
        }
        return groups.size
    }

    private fun findByKey(rootKey:Int, key: Int) {
        val foundValues = groups[rootKey]
        if(key in programs) {
            val newValues = programs[key].toSet() - foundValues
            groups[rootKey] += newValues
            programs.remove(key)
            newValues.forEach { k ->
                findByKey(rootKey, k)
            }
        }
    }
}