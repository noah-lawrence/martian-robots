package com.martian.robots.domain.grid

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `the neighbour in each direction is one grid point away that way`() {
        val point = Point(x = 2, y = 3)

        assertSoftly {
            point.neighbour(towards = NORTH) shouldBe Point(x = 2, y = 4)
            point.neighbour(towards = EAST) shouldBe Point(x = 3, y = 3)
            point.neighbour(towards = SOUTH) shouldBe Point(x = 2, y = 2)
            point.neighbour(towards = WEST) shouldBe Point(x = 1, y = 3)
        }
    }
}
