package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.instruction.MoveForward
import com.martian.robots.domain.instruction.TurnLeft
import com.martian.robots.domain.instruction.TurnRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class InstructionParserTest {

    @Test
    fun `the standard instruction letters are parsed in order`() {
        parseInstructions(value = "LRF") shouldBe listOf(TurnLeft, TurnRight, MoveForward)
    }

    @Test
    fun `an empty instruction line is parsed as no instructions`() {
        parseInstructions(value = "") shouldBe emptyList()
    }

    @ParameterizedTest
    // Lowercase letters are rejected
    @ValueSource(chars = ['X', 'f'])
    fun `an unknown instruction letter is rejected with a clear message`(letter: Char) {
        shouldThrow<IllegalArgumentException> {
            parseInstructions(value = "F${letter}F")
        }.message shouldBe "Unknown instruction '$letter'"
    }
}
