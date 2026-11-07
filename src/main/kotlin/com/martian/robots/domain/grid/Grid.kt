package com.martian.robots.domain.grid

/** The rectangular surface the robots move on, from [lowerLeft] to [upperRight] inclusive. */
data class Grid(val lowerLeft: Point, val upperRight: Point) {

    /** Whether [point] is on the grid, including its edges. */
    operator fun contains(point: Point): Boolean =
        point.x in lowerLeft.x..upperRight.x &&
            point.y in lowerLeft.y..upperRight.y
}
