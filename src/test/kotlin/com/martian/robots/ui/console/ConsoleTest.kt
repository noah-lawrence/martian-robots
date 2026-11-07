package com.martian.robots.ui.console

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Runs the whole application as a user would: text goes in, and the printed output, errors
 * and exit code come out. In-memory streams stand in for stdin, stdout and stderr; nothing is mocked.
 */
class ConsoleTest {

    @Test
    fun `the sample input from the brief prints the expected output`() {
        val output = runConsoleWith(
            input = """
            5 3
            1 1 E
            RFRFRFRF

            3 2 N
            FRRFLLFFRRFLL

            0 3 W
            LLFFFLFLFL
            """.trimIndent() + "\n"
        )

        output.stdout shouldBe linesOf("1 1 E", "3 3 N LOST", "2 3 S")
        output.stderr shouldBe ""
        output.exitCode shouldBe 0
    }

    @Test
    fun `input without a trailing newline is read in full`() {
        // e.g. a file piped in whose last line has no newline
        val output = runConsoleWith(input = "5 3\n1 1 E\nF")

        output.stdout shouldBe linesOf("2 1 E")
    }

    @Test
    fun `input with Windows line endings is read correctly`() {
        val output = runConsoleWith(input = "5 3\r\n1 1 E\r\nF\r\n")

        output.stdout shouldBe linesOf("2 1 E")
    }

    @Test
    fun `invalid input prints an error message to stderr and exits with a failure code`() {
        val output = runConsoleWith(input = "5 3\n7 7 N\nF\n")

        output.stdout shouldBe ""
        output.stderr shouldBe linesOf("Error: Robot at 7 7 starts off the grid")
        output.exitCode shouldBe 1
    }

    @Test
    fun `invalid input after valid robots prints no results for any robot`() {
        // The first two robots are valid; the third has instructions of 100 characters, one over the limit
        val output = runConsoleWith(
            input = "5 3\n1 1 E\nF\n2 2 N\nF\n3 3 W\n" + "L".repeat(n = 100) + "\n"
        )

        output.stdout shouldBe ""
        output.stderr shouldBe linesOf("Error: Robot at 3 3 has instructions of 100 or more characters")
        output.exitCode shouldBe 1
    }

    @Test
    fun `empty input prints an error message to stderr and exits with a failure code`() {
        val output = runConsoleWith(input = "")

        output.stdout shouldBe ""
        output.stderr shouldBe linesOf("Error: Input is empty")
        output.exitCode shouldBe 1
    }

    private data class ConsoleRun(val exitCode: Int, val stdout: String, val stderr: String)

    private fun runConsoleWith(input: String): ConsoleRun {
        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()

        val exitCode = runConsole(
            input = ByteArrayInputStream(input.toByteArray()),
            output = PrintStream(stdout, true),
            errors = PrintStream(stderr, true)
        )

        return ConsoleRun(exitCode = exitCode, stdout = stdout.toString(), stderr = stderr.toString())
    }

    /** The text printed by println for each line, using the platform's line separator. */
    private fun linesOf(vararg lines: String): String =
        lines.joinToString(separator = "") { it + System.lineSeparator() }
}
