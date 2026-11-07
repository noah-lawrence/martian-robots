package com.martian.robots.ui.text.parsing

import com.martian.robots.domain.grid.Direction.EAST
import com.martian.robots.domain.grid.Direction.NORTH
import com.martian.robots.domain.grid.Direction.SOUTH
import com.martian.robots.domain.grid.Direction.WEST
import com.martian.robots.domain.grid.Grid
import com.martian.robots.domain.mission.RobotMission
import com.martian.robots.domain.robot.Robot
import com.martian.robots.helpers.grid
import com.martian.robots.helpers.instructionsOf
import com.martian.robots.helpers.robot
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class InputParserTest {

    @Test
    fun `a grid line gives a grid from 0, 0 to the upper-right coordinates`() {
        parseInput(lines = listOf("5 3")).grid shouldBe grid(x = 5, y = 3)
    }

    @Test
    fun `a grid line without exactly two values is rejected`() {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("5"))
        }.message shouldBe "Invalid first line input"
    }

    @Test
    fun `a robot line gives a robot at the coordinates with the orientation`() {
        parseInput(lines = listOf("50 50", "1 2 W", "F")).missions.single().robot shouldBe
            robot(x = 1, y = 2, orientation = WEST)
    }

    @Test
    fun `a robot line without exactly three values is rejected`() {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("50 50", "1 E", "F"))
        }.message shouldBe "Invalid robot input"
    }

    @Test
    fun `a robot line with multi-digit coordinates is read as whole numbers`() {
        parseInput(lines = listOf("50 50", "10 12 S", "F")).missions.single().robot shouldBe
            robot(x = 10, y = 12, orientation = SOUTH)
    }

    @Test
    fun `a robot line with a multi-letter direction is rejected`() {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("50 50", "1 1 EAST", "F"))
        }.message shouldBe "Invalid robot input"
    }

    @Test
    fun `uneven whitespace between and around values is accepted`() {
        parseInput(lines = listOf("  5   3 ", "\t1  1\tE ", " RFRF RFRF ")) shouldBe ExplorationInput(
            grid = grid(x = 5, y = 3),
            missions = listOf(
                RobotMission(
                    robot = robot(x = 1, y = 1, orientation = EAST),
                    instructions = instructionsOf(letters = "RFRFRFRF"),
                ),
            )
        )
    }

    @Test
    fun `the input is parsed into the grid and one mission per robot`() {
        val lines = listOf(
            "5 3",
            "1 1 E",
            "RFRFRFRF",
            "3 2 N",
            "FRRFLLFFRRFLL",
        )

        parseInput(lines = lines) shouldBe ExplorationInput(
            grid = grid(x = 5, y = 3),
            missions = listOf(
                RobotMission(
                    robot = robot(x = 1, y = 1, orientation = EAST),
                    instructions = instructionsOf(letters = "RFRFRFRF"),
                ),
                RobotMission(
                    robot = robot(x = 3, y = 2, orientation = NORTH),
                    instructions = instructionsOf(letters = "FRRFLLFFRRFLL"),
                ),
            )
        )
    }

    @Test
    fun `an input with only a grid line has no missions`() {
        parseInput(lines = listOf("5 3")) shouldBe ExplorationInput(grid = grid(x = 5, y = 3), missions = emptyList())
    }

    // Blank lines

    @Test
    fun `blank and whitespace-only lines anywhere in the input are ignored`() {
        val lines = listOf("", "  ", "5 3", "\t", "1 1 E", "", "F", "   ")

        parseInput(lines = lines) shouldBe ExplorationInput(
            grid = grid(x = 5, y = 3),
            missions = listOf(
                RobotMission(
                    robot = robot(x = 1, y = 1, orientation = EAST),
                    instructions = instructionsOf(letters = "F"),
                ),
            )
        )
    }

    // Grid size limit

    @Test
    fun `a grid of 0 by 0 is accepted`() {
        parseInput(lines = listOf("0 0")).grid shouldBe grid(x = 0, y = 0)
    }

    @Test
    fun `a grid of up to 50 by 50 is accepted`() {
        parseInput(lines = listOf("50 50")).grid shouldBe grid(x = 50, y = 50)
    }

    @ParameterizedTest
    @ValueSource(strings = ["51 3", "5 51", "51 51"])
    fun `a grid coordinate greater than 50 is rejected`(gridLine: String) {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf(gridLine))
        }.message shouldBe "Grid coordinates must not be greater than 50"
    }

    // Instruction length limit

    @Test
    fun `instructions of up to 99 characters are accepted`() {
        val instructions = "F".repeat(n = 99)

        parseInput(lines = listOf("5 3", "1 1 E", instructions)).missions shouldBe listOf(
            RobotMission(
                robot = robot(x = 1, y = 1, orientation = EAST),
                instructions = instructionsOf(letters = instructions),
            ),
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [100, 150])
    fun `instructions of 100 or more characters are rejected`(length: Int) {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("5 3", "1 1 E", "F".repeat(n = length)))
        }.message shouldBe "Robot at 1 1 has instructions of 100 or more characters"
    }

    @Test
    fun `whitespace between instructions does not count towards the limit`() {
        // 99 letters with a space between each one: 197 characters on the line
        val instructionLine = List(size = 99) { "F" }.joinToString(separator = " ")

        parseInput(lines = listOf("5 3", "1 1 E", instructionLine)).missions shouldBe listOf(
            RobotMission(
                robot = robot(x = 1, y = 1, orientation = EAST),
                instructions = instructionsOf(letters = "F".repeat(n = 99)),
            ),
        )
    }

    // Robots starting off the grid

    @Test
    fun `a robot starting on the edge of the grid is accepted`() {
        parseInput(lines = listOf("5 3", "5 3 N", "L")).missions shouldBe listOf(
            RobotMission(robot = robot(x = 5, y = 3, orientation = NORTH), instructions = instructionsOf(letters = "L"))
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["7 7 N", "6 0 E", "0 4 S", "-1 2 N"])
    fun `a robot starting off the grid is rejected`(robotLine: String) {
        val (x, y) = robotLine.split(" ")

        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("5 3", robotLine, "F"))
        }.message shouldBe "Robot at $x $y starts off the grid"
    }

    // Missing or malformed input

    @Test
    fun `an empty input is rejected`() {
        assertSoftly {
            for (lines in listOf(emptyList(), listOf(""), listOf("  ", "\t"))) {
                shouldThrow<IllegalArgumentException> {
                    parseInput(lines = lines)
                }.message shouldBe "Input is empty"
            }
        }
    }

    @Test
    fun `a final robot with no instruction line is rejected`() {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("5 3", "1 1 E", "F", "2 2 N"))
        }.message shouldBe "Robot at 2 2 has no instructions"
    }

    @ParameterizedTest
    @ValueSource(strings = ["a 3", "5 b", "5.5 3"])
    fun `grid coordinates that are not whole numbers are rejected`(gridLine: String) {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf(gridLine))
        }.message shouldBe "Grid coordinates must be whole numbers"
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1 3", "5 -1"])
    fun `negative grid coordinates are rejected`(gridLine: String) {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf(gridLine))
        }.message shouldBe "Grid coordinates must not be negative"
    }

    @ParameterizedTest
    @ValueSource(strings = ["a 1 N", "1 b N", "1.5 1 N"])
    fun `robot coordinates that are not whole numbers are rejected`(robotLine: String) {
        shouldThrow<IllegalArgumentException> {
            parseInput(lines = listOf("50 50", robotLine, "F"))
        }.message shouldBe "Robot coordinates must be whole numbers"
    }
}
