package com.martian.robots.domain.mission

import com.martian.robots.domain.instruction.Instruction
import com.martian.robots.domain.mars.Mars
import com.martian.robots.domain.mars.Scent
import com.martian.robots.domain.mission.RobotMission.Outcome.Completed
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost
import com.martian.robots.domain.robot.Robot

/** A robot sent out with a sequence of instructions. */
data class RobotMission(val robot: Robot, val instructions: List<Instruction>) {

    /**
     * Runs this mission on [mars]. Each instruction proposes the robot's next state:
     * - a move that stays on the grid goes ahead
     * - a move off the grid from a scented point is ignored
     * - any other move off the grid loses the robot, ending the mission at its last state on the grid.
     */
    fun run(mars: Mars): Outcome {
        var current = robot

        for (instruction in instructions) {
            val proposed = instruction.applyTo(robot = current)
            when {
                proposed.location in mars.grid -> current = proposed
                Scent(location = current.location) in mars.scents -> continue
                else -> return Lost(robot = current)
            }
        }

        return Completed(robot = current)
    }

    /** How a mission ended. */
    sealed interface Outcome {
        /** Every instruction was carried out; [robot] is the robot's final state. */
        data class Completed(val robot: Robot) : Outcome

        /** The robot fell off the grid; [robot] is its last state on the grid. */
        data class Lost(val robot: Robot) : Outcome
    }
}
