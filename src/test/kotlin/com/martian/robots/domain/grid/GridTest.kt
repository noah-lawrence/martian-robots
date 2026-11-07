package com.martian.robots.domain.grid

import com.martian.robots.helpers.grid
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GridTest {

    private val grid = grid(x = 1, y = 1)

    @Test
    fun `every corner of the grid is on the grid`() {
        assertSoftly {
            grid.contains(point = Point(x = 0, y = 0)) shouldBe true
            grid.contains(point = Point(x = 0, y = 1)) shouldBe true
            grid.contains(point = Point(x = 1, y = 0)) shouldBe true
            grid.contains(point = Point(x = 1, y = 1)) shouldBe true
        }
    }

    @Test
    fun `a point just past any edge is off the grid`() {
        assertSoftly {
            grid.contains(point = Point(x = 0, y = 2)) shouldBe false
            grid.contains(point = Point(x = 2, y = 0)) shouldBe false
            grid.contains(point = Point(x = -1, y = 0)) shouldBe false
            grid.contains(point = Point(x = 0, y = -1)) shouldBe false
        }
    }
}
