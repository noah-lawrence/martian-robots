package com.martian.robots.helpers

import com.martian.robots.domain.grid.Grid
import com.martian.robots.domain.grid.Point

/** A [Grid] from (0, 0) to ([x], [y]), as described in the brief, for writing tests concisely. */
fun grid(x: Int, y: Int): Grid =
    Grid(lowerLeft = Point(x = 0, y = 0), upperRight = Point(x = x, y = y))
