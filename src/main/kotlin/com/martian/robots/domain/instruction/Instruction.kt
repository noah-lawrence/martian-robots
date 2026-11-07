package com.martian.robots.domain.instruction

import com.martian.robots.domain.robot.Robot

/**
 * An instruction sent from Earth to a robot. Each instruction works out the robot's planned
 * next state; whether that move goes ahead is decided when the mission is run.
 */
sealed interface Instruction {

    /** Returns the new state [robot] would be in after this instruction. */
    fun applyTo(robot: Robot): Robot
}
