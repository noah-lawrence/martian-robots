package com.martian.robots.domain.grid

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DirectionTest {

    @Test
    fun `clockwise gives the next direction round the compass`() {
        assertSoftly {
            NORTH.clockwise() shouldBe EAST
            EAST.clockwise() shouldBe SOUTH
            SOUTH.clockwise() shouldBe WEST
            WEST.clockwise() shouldBe NORTH
        }
    }

    @Test
    fun `anticlockwise gives the previous direction round the compass`() {
        assertSoftly {
            NORTH.anticlockwise() shouldBe WEST
            WEST.anticlockwise() shouldBe SOUTH
            SOUTH.anticlockwise() shouldBe EAST
            EAST.anticlockwise() shouldBe NORTH
        }
    }
}
