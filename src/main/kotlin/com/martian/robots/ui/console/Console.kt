package com.martian.robots.ui.console

import com.martian.robots.application.exploreMars
import com.martian.robots.domain.mars.Mars
import com.martian.robots.ui.text.formatting.formatResult
import com.martian.robots.ui.text.parsing.parseInput
import java.io.InputStream
import java.io.PrintStream

/** The exit code when every mission was run. */
const val EXIT_SUCCESS = 0

/** The exit code when the input was invalid. */
const val EXIT_INVALID_INPUT = 1

/**
 * Reads the input from [input], explores Mars, and prints one line per robot to [output].
 * In the running application these are stdin and stdout.
 *
 * If the input is invalid, prints `Error: <reason>` to [errors] (stderr) instead and returns
 * [EXIT_INVALID_INPUT]. Any other exception is a bug, so it is not caught.
 *
 * @return the exit code for the process
 */
fun runConsole(input: InputStream, output: PrintStream, errors: PrintStream): Int {
    val lines = input.bufferedReader().readLines()

    val results = try {
        val parsed = parseInput(lines = lines)
        val outcomes = exploreMars(mars = Mars(grid = parsed.grid), missions = parsed.missions)
        outcomes.map(::formatResult)
    } catch (e: IllegalArgumentException) {
        errors.println("Error: ${e.message}")
        return EXIT_INVALID_INPUT
    }

    results.forEach { output.println(it) }
    return EXIT_SUCCESS
}
