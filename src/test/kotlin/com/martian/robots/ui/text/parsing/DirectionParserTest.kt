package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DirectionParserTest {

    @Test
    fun `each direction letter is parsed`() {
        assertSoftly {
            parseDirection(letter = 'N') shouldBe NORTH
            parseDirection(letter = 'E') shouldBe EAST
            parseDirection(letter = 'S') shouldBe SOUTH
            parseDirection(letter = 'W') shouldBe WEST
        }
    }

    @ParameterizedTest
    // Lowercase letters are rejected: the brief specifies N, S, E and W
    @ValueSource(chars = ['X', 'n'])
    fun `an unknown direction letter is rejected with a clear message`(letter: Char) {
        shouldThrow<IllegalArgumentException> {
            parseDirection(letter = letter)
        }.message shouldBe "Unknown orientation '$letter'"
    }
}
