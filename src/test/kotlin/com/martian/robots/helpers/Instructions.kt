package com.martian.robots.helpers

import com.martian.robots.domain.instruction.Instruction
import com.martian.robots.domain.instruction.MoveForward
import com.martian.robots.domain.instruction.TurnLeft
import com.martian.robots.domain.instruction.TurnRight

/**
 * Builds instructions from the brief's letters, for writing tests concisely without
 * depending on the text format's parser.
 */
fun instructionsOf(letters: String): List<Instruction> = letters.map { letter ->
    when (letter) {
        'L' -> TurnLeft
        'R' -> TurnRight
        'F' -> MoveForward
        else -> error("No test instruction for '$letter'")
    }
}
