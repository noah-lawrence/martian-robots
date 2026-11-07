package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.instruction.Instruction
import com.martian.robots.domain.instruction.MoveForward
import com.martian.robots.domain.instruction.TurnLeft
import com.martian.robots.domain.instruction.TurnRight

/** The letters used in the text format for the instructions in the brief. */
private val STANDARD_INSTRUCTIONS: Map<Char, Instruction> = mapOf(
    'L' to TurnLeft,
    'R' to TurnRight,
    'F' to MoveForward,
)

/** Parses an instruction line, e.g. `FRRFL`, using the letters in [STANDARD_INSTRUCTIONS]. */
fun parseInstructions(value: String): List<Instruction> = value.map { letter ->
    STANDARD_INSTRUCTIONS[letter] ?: throw IllegalArgumentException("Unknown instruction '$letter'")
}
