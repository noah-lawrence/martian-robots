package com.martian.robots.ui.text.formatting

import com.martian.robots.domain.grid.Direction
import com.martian.robots.ui.text.LETTERS_BY_DIRECTION

/** Formats [direction] as its letter in the text format, e.g. `N` for north. */
fun formatDirection(direction: Direction): Char =
    LETTERS_BY_DIRECTION.getValue(direction)
