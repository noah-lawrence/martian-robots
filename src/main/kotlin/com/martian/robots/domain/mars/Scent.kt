package com.martian.robots.domain.mars

import com.martian.robots.domain.grid.Point

/** The scent left at the last grid point a lost robot occupied before falling off. */
data class Scent(val location: Point)
