package com.martian.robots.domain.robot

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import com.martian.robots.helpers.robot
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RobotTest {

    @Test
    fun `turning left changes the orientation anticlockwise and keeps the location`() {
        robot(x = 1, y = 1, orientation = NORTH).turnLeft() shouldBe robot(x = 1, y = 1, orientation = WEST)
    }

    @Test
    fun `turning right changes the orientation clockwise and keeps the location`() {
        robot(x = 1, y = 1, orientation = NORTH).turnRight() shouldBe robot(x = 1, y = 1, orientation = EAST)
    }

    @Test
    fun `moving forward moves one grid point in the direction faced and keeps the orientation`() {
        assertSoftly {
            robot(x = 1, y = 1, orientation = NORTH).moveForward() shouldBe robot(x = 1, y = 2, orientation = NORTH)
            robot(x = 1, y = 1, orientation = EAST).moveForward() shouldBe robot(x = 2, y = 1, orientation = EAST)
            robot(x = 1, y = 1, orientation = SOUTH).moveForward() shouldBe robot(x = 1, y = 0, orientation = SOUTH)
            robot(x = 1, y = 1, orientation = WEST).moveForward() shouldBe robot(x = 0, y = 1, orientation = WEST)
        }
    }
}
