package com.martian.robots.domain.instruction

import com.martian.robots.domain.robot.Robot

/** Moves the robot one grid point in the direction it is facing. */
data object MoveForward : Instruction {
    override fun applyTo(robot: Robot): Robot = robot.moveForward()
}
