package com.martian.robots

import com.martian.robots.ui.console.runConsole
import kotlin.system.exitProcess

/** Starts the application on the console, exiting with code 0 on success or 1 for invalid input. */
fun main() {
    exitProcess(runConsole(input = System.`in`, output = System.out, errors = System.err))
}
