<div align="center">

# 🤖 Martian Robots
Welcome to the martian robots project.
________________
</div>

Robots explore the surface of Mars (modelled as a rectangular grid), following instructions sent from Earth.
For each robot, the program reports its final position and orientation, or `LOST` if it falls off the edge of the grid. 
A lost robot leaves a scent that stops later robots from falling off at the same point.

### 📦 Requirements

* JDK 21 (the build uses a Kotlin JVM toolchain for Java 21)
* Nothing else: the Gradle wrapper (`./gradlew`) is included and downloads Gradle itself

On Windows, use `gradlew.bat` in place of `./gradlew` in the commands below.

### ▶️ Run the application

The application reads its input from stdin and prints one line per robot to stdout.

#### From an input file (recommended)

```shell
./gradlew run -q < input.txt
```

#### From your IDE

Open the project in an IDE with Kotlin support, such as IntelliJ IDEA, and run the `main` function in `src/main/kotlin/com/martian/robots/Main.kt`. 
Then type or paste the input into the Run window, and end it with `Cmd+D` on Mac or `Ctrl+D` on Windows and Linux.

#### Example

Input (the sample from the brief):

```
5 3
1 1 E
RFRFRFRF

3 2 N
FRRFLLFFRRFLL

0 3 W
LLFFFLFLFL
```

Output:

```
1 1 E
3 3 N LOST
2 3 S
```

The first line is the upper-right corner of the grid (the lower-left is `0 0`). 
Each robot then has two lines: its starting position and orientation (`N`, `E`, `S` or `W`), and its instructions (`L` turn left, `R` turn right, `F` move forward).

If the input is invalid, the application prints a message such as `Error: Robot at 7 7 starts off the grid` to stderr and exits with code 1.

### 🔧 Build the application

```shell
./gradlew clean build
```

This compiles the code and runs all the tests.

### 🏢 Structure

The code is structured following a clean architecture approach, with dependencies pointing inwards: `ui` depends on `application`, which depends on `domain`.

* The `domain` contains the models and the rules from the brief:
  * `grid`: `Grid`, `Point` and `Direction`.
  * `robot`: a `Robot`'s position and orientation (the `Direction` it is facing), and how it turns and moves.
  * `instruction`: the instructions (`TurnLeft`, `TurnRight`, `MoveForward`).
  * `mars`: `Mars`, which holds the grid and the `Scent`s left by lost robots.
  * `mission`: a `RobotMission` (a robot and its instructions), whose `run` applies the rules for the grid edge and scents, and the `RobotMission.Outcome` (`Completed` or `Lost`) it returns.
* The `application` contains the use cases: the things the service can do, each built from the domain's models and rules. 
  There is one so far:
  * `exploreMars` (in `ExploreMars.kt`) is given a `Mars` and the missions, runs each robot's mission in turn, leaving a scent whenever a robot is lost, and returns the outcomes.

  A new capability, such as reporting which grid points have scents, would be a new use case here, which any user interface could then call.
* The `ui` contains the interfaces for using the service, each of which turns its input into calls to the use cases:
  * `ui/text` handles the brief's text format. 
    `parsing` reads the input, and `formatting` writes the output. 
    The direction letters they share live in `DirectionLetters.kt`, so input and output can't disagree.
  * `ui/console` reads the input from stdin, parses it with `ui/text`, calls `exploreMars`, formats the outcomes with `ui/text`, prints them to stdout, and reports invalid input.
* `Main.kt` is the entry point. 
  It starts the console and exits with its exit code.

#### Why the domain doesn't use the input letters

The domain names things in full: `Direction.NORTH` rather than `N`, and `TurnLeft`, `TurnRight` and `MoveForward` rather than `L`, `R` and `F`. 
The single letters belong to the brief's text format, so they live only in `ui/text`, which converts between letters and domain values at the edge: `DirectionLetters.kt` holds the direction letters, and `STANDARD_INSTRUCTIONS` in `ui/text/parsing/InstructionParser.kt` holds the instruction letters.

This keeps the domain independent of how input is written:

* **The domain reads in the language of the problem.** 
  Code such as `NORTH.clockwise()` or `Lost(robot = ...)` says what it means without knowing any input format.
* **A change to the input format doesn't touch the domain.** 
  If the letters changed, or the input became JSON with `"north"` and `"turnLeft"`, only the parsing and formatting in `ui` would change. 
  The rules for moving, turning and scents, and their tests, would stay as they are.
* **Each interface can have its own format.** 
  A REST endpoint (see [Adding an interface](#-adding-an-interface)) could accept a different format from the console, and both would convert to the same domain values.

### 📋 Assumptions

* **Blank lines are ignored** wherever they appear, including between robots, as in the brief's sample input. 
  Lines containing only spaces or tabs count as blank. 
  As a result, every robot needs at least one instruction: an empty instruction line counts as blank too.
* **Grid coordinates must not be greater than 50**, the brief's maximum. 
  A larger grid is rejected with an error.
* **A robot must start on the grid.** 
  A robot whose starting position is off the grid is rejected with an error, rather than being reported as lost.
* **Every robot must have an instruction line.** 
  A robot position at the end of the input with no instructions after it is rejected with an error.
* **Letters are case-sensitive.** 
  Orientations and instructions must be uppercase, as in the brief: `n` or `f`, for example, is rejected with an error.
* **Instructions must be less than 100 characters**, the brief's limit. 
  Longer instructions are rejected with an error. 
  Whitespace between instructions isn't counted, so `RFRF RFRF` is 8 characters.
* **Invalid input is reported, not crashed on.** 
  Any input that breaks these rules prints `Error: <reason>` to stderr, prints nothing to stdout, and exits with code 1. 
  A successful run exits with code 0.

### ➕ Adding an instruction

Every instruction is an object implementing the sealed `Instruction` interface in `domain/instruction`, and works out the robot's planned next state:

```kotlin
data object MoveForward : Instruction {
    override fun applyTo(robot: Robot): Robot = robot.moveForward()
}
```

To add an instruction, create a new object like this in `domain/instruction`, then give it a letter in the text format by adding it to `STANDARD_INSTRUCTIONS` in `ui/text/parsing/InstructionParser.kt`. 
The letters are part of the text format, so the domain doesn't know about them. 
Nothing else changes: the rules for the grid edge and scents are applied to whatever an instruction proposes.

### 🌐 Adding an interface

The console is only one way in. 
Another interface, such as a REST endpoint, would be a new package under `ui` (e.g. `ui/rest`) that turns its requests into calls to the use cases. 
`domain` and `application` don't change, and the web framework (e.g. Ktor or Spring) is a dependency of `ui/rest` only.

### 🧪 Tests

Run the tests with `./gradlew test`.

Tests mirror the main packages, so the test for a piece of code lives in the matching package. 
They use real collaborators rather than mocks, so each test checks real behaviour. 
`ConsoleTest` runs the whole application end to end through the available interface, with in-memory streams standing in for stdin and stdout. 
Small builders for test data (`robot(...)`, `grid(...)`, `instructionsOf(...)`) live in `helpers`.
