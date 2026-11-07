package com.martian.robots.application

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import com.martian.robots.domain.grid.Point
import com.martian.robots.domain.mars.Mars
import com.martian.robots.domain.mars.Scent
import com.martian.robots.domain.mission.RobotMission
import com.martian.robots.domain.mission.RobotMission.Outcome.Completed
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost
import com.martian.robots.helpers.grid
import com.martian.robots.helpers.instructionsOf
import com.martian.robots.helpers.robot
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ExploreMarsTest {

    @Test
    fun `the sample input from the brief gives the expected outcomes`() {
        val missions = listOf(
            RobotMission(
                robot = robot(x = 1, y = 1, orientation = EAST),
                instructions = instructionsOf(letters = "RFRFRFRF"),
            ),
            RobotMission(
                robot = robot(x = 3, y = 2, orientation = NORTH),
                instructions = instructionsOf(letters = "FRRFLLFFRRFLL"),
            ),
            // Saved by the scent robot 2 leaves at (3, 3)
            RobotMission(
                robot = robot(x = 0, y = 3, orientation = WEST),
                instructions = instructionsOf(letters = "LLFFFLFLFL"),
            ),
        )

        exploreMars(mars = Mars(grid = grid(x = 5, y = 3)), missions = missions) shouldBe listOf(
            Completed(robot = robot(x = 1, y = 1, orientation = EAST)),
            Lost(robot = robot(x = 3, y = 3, orientation = NORTH)),
            Completed(robot = robot(x = 2, y = 3, orientation = SOUTH)),
        )
    }

    @Test
    fun `a lost robot leaves a scent that saves a later robot from the same point`() {
        val missions = listOf(
            RobotMission(
                robot = robot(x = 3, y = 2, orientation = NORTH),
                instructions = instructionsOf(letters = "FF"),
            ),
            RobotMission(
                robot = robot(x = 3, y = 3, orientation = NORTH),
                instructions = instructionsOf(letters = "F"),
            ),
        )

        exploreMars(mars = Mars(grid = grid(x = 5, y = 3)), missions = missions) shouldBe listOf(
            Lost(robot = robot(x = 3, y = 3, orientation = NORTH)),
            Completed(robot = robot(x = 3, y = 3, orientation = NORTH)),
        )
    }

    @Test
    fun `a robot that completes its instructions leaves no scent`() {
        // The first robot ends on the edge without leaving it, so the second is still lost from there
        val missions = listOf(
            RobotMission(
                robot = robot(x = 3, y = 2, orientation = NORTH),
                instructions = instructionsOf(letters = "F"),
            ),
            RobotMission(
                robot = robot(x = 3, y = 3, orientation = NORTH),
                instructions = instructionsOf(letters = "F"),
            ),
        )

        exploreMars(mars = Mars(grid = grid(x = 5, y = 3)), missions = missions) shouldBe listOf(
            Completed(robot = robot(x = 3, y = 3, orientation = NORTH)),
            Lost(robot = robot(x = 3, y = 3, orientation = NORTH)),
        )
    }

    @Test
    fun `when there are no missions then there are no outcomes`() {
        exploreMars(mars = Mars(grid = grid(x = 5, y = 3)), missions = emptyList()) shouldBe emptyList()
    }

    @Test
    fun `scents already on mars are respected by robots`() {
        val mars = Mars(grid = grid(x = 5, y = 3), scents = setOf(Scent(location = Point(x = 3, y = 3))))

        exploreMars(
            mars = mars,
            missions = listOf(
                RobotMission(
                    robot = robot(x = 3, y = 2, orientation = NORTH),
                    instructions = instructionsOf(letters = "FF"),
                ),
            )
        ) shouldBe listOf(Completed(robot = robot(x = 3, y = 3, orientation = NORTH)))
    }
}
