package com.martian.robots.domain.mission

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.WEST
import com.martian.robots.domain.grid.Point
import com.martian.robots.domain.mars.Mars
import com.martian.robots.domain.mars.Scent
import com.martian.robots.domain.mission.RobotMission.Outcome.Completed
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost
import com.martian.robots.domain.robot.Robot
import com.martian.robots.helpers.grid
import com.martian.robots.helpers.instructionsOf
import com.martian.robots.helpers.robot
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RobotMissionTest {

    private val grid = grid(x = 5, y = 3)

    @Test
    fun `when there are no instructions then the robot completes where it started`() {
        RobotMission(robot = robot(x = 1, y = 1, orientation = WEST), instructions = emptyList()).run(
            mars = Mars(grid = grid)
        ) shouldBe Completed(robot = robot(x = 1, y = 1, orientation = WEST))
    }

    @Test
    fun `when a move loses the robot then it is lost at its state before that move and the remaining instructions are not run`() {
        // If the runner carried on after losing the robot, it would turn right and move to (1, 3)
        RobotMission(
            robot = robot(x = 0, y = 3, orientation = NORTH),
            instructions = instructionsOf(letters = "FRF"),
        ).run(
            mars = Mars(grid = grid)
        ) shouldBe Lost(robot = robot(x = 0, y = 3, orientation = NORTH))
    }

    @Test
    fun `when a move off the grid is ignored then the robot stays put and carries on with the next instruction`() {
        RobotMission(
            robot = robot(x = 0, y = 3, orientation = NORTH),
            instructions = instructionsOf(letters = "FRF"),
        ).run(
            mars = Mars(grid = grid, scents = setOf(Scent(location = Point(x = 0, y = 3))))
        ) shouldBe Completed(robot = robot(x = 1, y = 3, orientation = EAST))
    }

    // The movement rules, one instruction at a time

    @Test
    fun `a move that stays on the grid goes ahead`() {
        moveForwardOnce(robot = robot(x = 1, y = 1, orientation = NORTH)) shouldBe
            Completed(robot = robot(x = 1, y = 2, orientation = NORTH))
    }

    @Test
    fun `a move that stays on the grid goes ahead from a scented point`() {
        moveForwardOnce(
            robot = robot(x = 0, y = 3, orientation = EAST),
            scents = setOf(Scent(location = Point(x = 0, y = 3))),
        ) shouldBe Completed(robot = robot(x = 1, y = 3, orientation = EAST))
    }

    @Test
    fun `a move off the grid from an unscented point loses the robot`() {
        moveForwardOnce(robot = robot(x = 3, y = 3, orientation = NORTH)) shouldBe
            Lost(robot = robot(x = 3, y = 3, orientation = NORTH))
    }

    @Test
    fun `a move off the grid from a scented point is ignored`() {
        moveForwardOnce(
            robot = robot(x = 3, y = 3, orientation = NORTH),
            scents = setOf(Scent(location = Point(x = 3, y = 3))),
        ) shouldBe Completed(robot = robot(x = 3, y = 3, orientation = NORTH))
    }

    @Test
    fun `a scent only protects its own grid point`() {
        moveForwardOnce(
            robot = robot(x = 0, y = 2, orientation = WEST),
            scents = setOf(Scent(location = Point(x = 0, y = 3))),
        ) shouldBe Lost(robot = robot(x = 0, y = 2, orientation = WEST))
    }

    @Test
    fun `on a 0 by 0 grid a robot can turn but any move forward loses it`() {
        // The grid is a single point, so turning keeps the robot on it and moving forward leaves it
        RobotMission(
            robot = robot(x = 0, y = 0, orientation = NORTH),
            instructions = instructionsOf(letters = "RF"),
        ).run(
            mars = Mars(grid = grid(x = 0, y = 0))
        ) shouldBe Lost(robot = robot(x = 0, y = 0, orientation = EAST))
    }

    /** Runs a single forward move on the default 5 x 3 grid. */
    private fun moveForwardOnce(robot: Robot, scents: Set<Scent> = emptySet()): RobotMission.Outcome =
        RobotMission(robot = robot, instructions = instructionsOf(letters = "F")).run(
            mars = Mars(grid = grid, scents = scents)
        )
}
