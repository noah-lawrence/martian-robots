package com.martian.robots.ui.text.formatting

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DirectionFormatterTest {

    @Test
    fun `each direction is formatted as its letter`() {
        assertSoftly {
            formatDirection(direction = NORTH) shouldBe 'N'
            formatDirection(direction = EAST) shouldBe 'E'
            formatDirection(direction = SOUTH) shouldBe 'S'
            formatDirection(direction = WEST) shouldBe 'W'
        }
    }
}
