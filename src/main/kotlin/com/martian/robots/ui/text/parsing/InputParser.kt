package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.grid.Grid
import com.martian.robots.domain.grid.Point
import com.martian.robots.domain.mission.RobotMission
import com.martian.robots.domain.robot.Robot

/** The largest value allowed for any coordinate. */
private const val MAX_COORDINATE = 50

/** Instruction strings must be shorter than this. */
private const val INSTRUCTION_LENGTH_LIMIT = 100

/** Everything read from the input: the grid, and a mission for each robot. */
data class ExplorationInput(val grid: Grid, val missions: List<RobotMission>)

/**
 * Parses the full input: a grid line, followed by two lines per robot (its position,
 * then its instructions). Blank lines are ignored wherever they appear.
 *
 * Instructions must be shorter than [INSTRUCTION_LENGTH_LIMIT] characters, not counting
 * whitespace between them.
 */
fun parseInput(lines: List<String>): ExplorationInput {
    val contentLines = lines.filter { it.isNotBlank() }
    require(contentLines.isNotEmpty()) { "Input is empty" }
    val grid = parseGrid(line = contentLines.first())

    val missions = contentLines.drop(1).chunked(2).map { robotLines ->
        val robot = parseRobot(line = robotLines[0])
        val position = "${robot.location.x} ${robot.location.y}"
        require(robot.location in grid) { "Robot at $position starts off the grid" }
        val instructionLine = requireNotNull(robotLines.getOrNull(1)) { "Robot at $position has no instructions" }
        val instructions = instructionLine.removeWhitespace()
        require(instructions.length < INSTRUCTION_LENGTH_LIMIT) {
            "Robot at $position has instructions of $INSTRUCTION_LENGTH_LIMIT or more characters"
        }
        RobotMission(robot = robot, instructions = parseInstructions(value = instructions))
    }

    return ExplorationInput(grid = grid, missions = missions)
}

/** Parses the grid line, e.g. `5 3`: the upper-right coordinates, with the lower-left at 0, 0. */
private fun parseGrid(line: String): Grid {
    val values = line.splitOnWhitespace()
    require(values.size == 2) { "Invalid first line input" }
    val (maxX, maxY) = values.map { requireNotNull(it.toIntOrNull()) { "Grid coordinates must be whole numbers" } }
    require(maxX >= 0 && maxY >= 0) { "Grid coordinates must not be negative" }
    require(maxX <= MAX_COORDINATE && maxY <= MAX_COORDINATE) {
        "Grid coordinates must not be greater than $MAX_COORDINATE"
    }
    return Grid(
        lowerLeft = Point(x = 0, y = 0),
        upperRight = Point(x = maxX, y = maxY),
    )
}

/** Parses a robot line, e.g. `1 1 E`: its coordinates and orientation. */
private fun parseRobot(line: String): Robot {
    val values = line.splitOnWhitespace()
    require(values.size == 3 && values[2].length == 1) { "Invalid robot input" }
    val (x, y) = values.take(2).map { requireNotNull(it.toIntOrNull()) { "Robot coordinates must be whole numbers" } }
    return Robot(
        location = Point(x = x, y = y),
        orientation = parseDirection(letter = values[2].single())
    )
}

/** Any run of one or more whitespace characters. */
private val WHITESPACE = Regex("\\s+")

/** This string with all whitespace removed, e.g. `F R F` becomes `FRF`. */
private fun String.removeWhitespace() = this.replace(WHITESPACE, "")

/** The values in this string, separated by any amount of whitespace, ignoring leading and trailing whitespace. */
private fun String.splitOnWhitespace(): List<String> = this.trim().split(WHITESPACE)
