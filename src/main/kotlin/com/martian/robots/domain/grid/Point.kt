package com.martian.robots.domain.grid

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST

/** A point on the grid. [x] increases to the east and [y] increases to the north. */
data class Point(val x: Int, val y: Int) {

    /**
     * The grid point next to this one in the given direction.
     * North is the direction from (x, y) to (x, y + 1).
     */
    fun neighbour(towards: Direction): Point = when (towards) {
        NORTH -> copy(y = y + 1)
        EAST -> copy(x = x + 1)
        SOUTH -> copy(y = y - 1)
        WEST -> copy(x = x - 1)
    }
}
