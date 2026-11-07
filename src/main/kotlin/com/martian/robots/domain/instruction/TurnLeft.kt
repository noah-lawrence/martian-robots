package com.martian.robots.domain.instruction

import com.martian.robots.domain.robot.Robot

/** Turns the robot 90 degrees left, staying on the current grid point. */
data object TurnLeft : Instruction {
    override fun applyTo(robot: Robot): Robot = robot.turnLeft()
}
