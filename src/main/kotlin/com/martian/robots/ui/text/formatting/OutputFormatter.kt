package com.martian.robots.ui.text.formatting

import com.martian.robots.domain.mission.RobotMission
import com.martian.robots.domain.mission.RobotMission.Outcome.Completed
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost
import com.martian.robots.domain.robot.Robot

/** Formats a mission's result as an output line, e.g. `1 1 E` or `3 3 N LOST`. */
fun formatResult(result: RobotMission.Outcome): String = when (result) {
    is Completed -> formatRobot(robot = result.robot)
    is Lost -> "${formatRobot(robot = result.robot)} LOST"
}

/** Formats a robot's position and orientation, e.g. `1 1 E`. */
private fun formatRobot(robot: Robot): String =
    "${robot.location.x} ${robot.location.y} ${formatDirection(direction = robot.orientation)}"
