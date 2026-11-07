package com.martian.robots.ui.text

import com.martian.robots.domain.grid.Direction
import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST

/**
 * The letters used in the text format for each direction. Parsing and formatting
 * both use this one map, so input and output can't disagree.
 */
internal val DIRECTION_LETTERS: Map<Char, Direction> = mapOf(
    'N' to NORTH,
    'E' to EAST,
    'S' to SOUTH,
    'W' to WEST,
)

/** The reverse of [DIRECTION_LETTERS], for formatting output. */
internal val LETTERS_BY_DIRECTION: Map<Direction, Char> =
    DIRECTION_LETTERS.entries.associate { (letter, direction) -> direction to letter }
