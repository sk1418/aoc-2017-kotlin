package utils

import utils.Direction.*

typealias Point = Pair<Int, Int>

enum class Direction {
    Left, Up, Right, Down;

    override fun toString() = name.first().toString()

    fun isHorizontal() = this == Left || this == Right
    fun isVertical() = this == Up || this == Down
    fun opposite() = ordinal.let { entries[(it + 2).let { if (it > 3) it - 4 else it }] }
    fun turn90() = ordinal.let { entries[if (it == 3) 0 else it + 1] }
    fun turn90Back() = ordinal.let { entries[if (it == 0) 3 else it - 1] }
}

fun Point.move(directionChar: Char, steps: Int = 1) = when (directionChar) {
    'U' -> move(Up, steps)
    'D' -> move(Down, steps)
    'L' -> move(Left, steps)
    'R' -> move(Right, steps)
    else -> error("Unknown direction char: $directionChar")
}

fun Point.move(direction: Direction, steps: Int = 1) = when (direction) {
    Up -> first to (second - steps)
    Down -> first to (second + steps)
    Left -> (first - steps) to second
    Right -> (first + steps) to second
}

fun Point.allAdjacents(includeDiagonals: Boolean = true) =
    buildList<Point> {
        this += listOf(move(Up), move(Left), move(Down), move(Right))
        if (includeDiagonals) {
            this += listOf(move(Up).move(Left), move(Up).move(Right), move(Down).move(Left), move(Down).move(Right))
        }
    }


//Matrix related:
open class Matrix<T : Any>(val maxX: Int, val maxY: Int, open val points: Map<Point, T>) {

    protected fun findOneByValue(value: T) = points.entries.first { it.value == value }.key
    protected fun findByValue(value: T) = points.filterValues { it == value }.keys

    protected fun Point.validPoint() = first in 0..maxX && second in 0..maxY
    protected fun Point.invalidPoint() = validPoint().not()

    protected fun Point.validAndExist() = validPoint() && this in points
    protected fun Point.notValidAndExist() = validAndExist().not()

    operator fun contains(pos: Pair<Int, Int>) = pos.validPoint()

    protected fun Point.safeMove(direction: Direction) = when (direction) {
        Up -> (first to ((second - 1).takeIf { it >= 0 } ?: 0))
        Down -> (first to ((second + 1).takeIf { it <= maxY } ?: maxY))
        Left -> (((first - 1).takeIf { it >= 0 } ?: 0) to second)
        Right -> (((first + 1).takeIf { it <= maxX } ?: maxX) to second)
    }

    protected fun Point.allAroundIn() =
        listOf(
            move(Up), move(Left), move(Down), move(Right),
            move(Up).move(Left), move(Up).move(Right), move(Down).move(Left), move(Down).move(Right)
        ).filter { it.validPoint() }

    override fun toString(): String = buildString {
        append('\n')
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                append(if (x to y in points) points[x to y] else ' ')
            }
            append('\n')
        }
    }

}