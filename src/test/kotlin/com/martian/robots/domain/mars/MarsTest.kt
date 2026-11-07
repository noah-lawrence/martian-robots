package com.martian.robots.domain.mars

import com.martian.robots.domain.grid.Point
import com.martian.robots.helpers.grid
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MarsTest {

    @Test
    fun `mars starts with no scents by default`() {
        Mars(grid = grid(x = 5, y = 3)).scents shouldBe emptySet()
    }

    @Test
    fun `leaving a scent returns a mars with the scent added`() {
        val mars = Mars(grid = grid(x = 5, y = 3), scents = setOf(Scent(location = Point(x = 0, y = 0))))

        mars.leaveScent(scent = Scent(location = Point(x = 3, y = 3))) shouldBe Mars(
            grid = grid(x = 5, y = 3),
            scents = setOf(
                Scent(location = Point(x = 0, y = 0)),
                Scent(location = Point(x = 3, y = 3)),
            ),
        )
    }
}
