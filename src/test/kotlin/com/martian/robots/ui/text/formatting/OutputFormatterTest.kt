package com.martian.robots.ui.text.formatting

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.mission.RobotMission.Outcome.Completed
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost
import com.martian.robots.helpers.robot
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OutputFormatterTest {

    @Test
    fun `a completed mission is formatted as the robot's final position and orientation`() {
        formatResult(result = Completed(robot = robot(x = 1, y = 1, orientation = EAST))) shouldBe "1 1 E"
    }

    @Test
    fun `a lost mission is formatted as the robot's last position and orientation followed by LOST`() {
        formatResult(result = Lost(robot = robot(x = 3, y = 3, orientation = NORTH))) shouldBe "3 3 N LOST"
    }

    @Test
    fun `multi-digit coordinates are formatted in full`() {
        formatResult(result = Completed(robot = robot(x = 50, y = 12, orientation = EAST))) shouldBe "50 12 E"
    }
}
