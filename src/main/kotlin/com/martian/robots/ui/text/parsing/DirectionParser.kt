package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.grid.Direction
import com.martian.robots.ui.text.DIRECTION_LETTERS

/** Parses a direction letter (`N`, `E`, `S` or `W`), rejecting any other letter, including lowercase. */
fun parseDirection(letter: Char): Direction =
    DIRECTION_LETTERS[letter] ?: throw IllegalArgumentException("Unknown orientation '$letter'")
