package com.martian.robots.domain.instruction

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.WEST
import com.martian.robots.helpers.robot
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class InstructionTest {

    @Test
    fun `turn left turns the robot left on the spot`() {
        TurnLeft.applyTo(robot = robot(x = 1, y = 1, orientation = NORTH)) shouldBe
            robot(x = 1, y = 1, orientation = WEST)
    }

    @Test
    fun `turn right turns the robot right on the spot`() {
        TurnRight.applyTo(robot = robot(x = 1, y = 1, orientation = NORTH)) shouldBe
            robot(x = 1, y = 1, orientation = EAST)
    }

    @Test
    fun `move forward moves the robot one grid point in the direction it is facing`() {
        MoveForward.applyTo(robot = robot(x = 1, y = 1, orientation = NORTH)) shouldBe
            robot(x = 1, y = 2, orientation = NORTH)
    }
}
