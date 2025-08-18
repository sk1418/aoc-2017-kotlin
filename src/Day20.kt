import utils.*
import kotlin.math.absoluteValue

// https://adventofcode.com/2017/day/20
fun main() {
    val today = "Day20"

    val input = readInput(today)
    val testInput = readTestInput(today)

    val re = "<[^>]*>".toRegex()
    fun List<String>.toParticles() = mapIndexed { idx, s ->
        val triples = re.findAll(s).map { it.value.drop(1).dropLast(1).toInts(",").let { Triple(it[0], it[1], it[2]) } }.toList()
        Particle(idx, triples[0], triples[1], triples[2])
    }.toMutableList()

    fun part1(input: List<String>): Int = Day20(input.toParticles()).findClosestParticle()
    fun part2(input: List<String>): Int = Day20(input.toParticles()).numOfParticlesAfter1k()

    chkTestInput(Part1, testInput, 0) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 2) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Particle(val idx: Int, var pos: Triple<Int, Int, Int>, var vel: Triple<Int, Int, Int>, val acc: Triple<Int, Int, Int>) {
    fun distanceTo0() = pos.first.absoluteValue + pos.second.absoluteValue + pos.third.absoluteValue
    private operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) = Triple(first + other.first, second + other.second, third + other.third)

    fun tick() {
        vel += acc
        pos += vel
    }
}

private data class Day20(var particles: MutableList<Particle>) {
    private fun tick1000() {
        repeat(1000) { particles.forEach { it.tick() } }
    }

    private fun tick1000WithCollisionRemoved() {
        repeat(1000) {
            particles.forEach { it.tick() }
            particles.groupBy { it.pos }.filterValues { it.size > 1 }
                .forEach { (_, v) -> particles.removeAll(v) }
        }
    }

    fun findClosestParticle(): Int {
        tick1000()
        return particles.minBy { it.distanceTo0() }.idx
    }

    fun numOfParticlesAfter1k(): Int {
        tick1000WithCollisionRemoved()
        return particles.size
    }
}