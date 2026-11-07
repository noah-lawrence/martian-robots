package com.martian.robots.domain.mars

import com.martian.robots.domain.grid.Grid

/**
 * Mars is modelled as a grid with scents left by lost robots.
 */
data class Mars(val grid: Grid, val scents: Set<Scent> = emptySet()) {

    /** Returns this Mars with [scent] added, so that later robots are not lost from the same grid point. */
    fun leaveScent(scent: Scent): Mars = copy(scents = scents + scent)
}
