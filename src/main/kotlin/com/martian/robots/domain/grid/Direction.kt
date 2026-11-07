package com.martian.robots.domain.grid

/** A compass direction. */
enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    /** The next direction clockwise round the compass, i.e. 90 degrees to the right. */
    fun clockwise(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }

    /** The next direction anticlockwise round the compass, i.e. 90 degrees to the left. */
    fun anticlockwise(): Direction = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }
}
