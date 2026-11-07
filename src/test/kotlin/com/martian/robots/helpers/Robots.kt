package com.martian.robots.helpers

import com.martian.robots.domain.grid.Direction
import com.martian.robots.domain.grid.Point
import com.martian.robots.domain.robot.Robot

/** A [Robot] at ([x], [y]) facing [orientation], for writing tests concisely. */
fun robot(x: Int, y: Int, orientation: Direction): Robot = Robot(
    location = Point(x = x, y = y),
    orientation = orientation
)
