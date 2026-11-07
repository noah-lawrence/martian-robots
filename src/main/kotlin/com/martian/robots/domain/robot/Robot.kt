package com.martian.robots.domain.robot

import com.martian.robots.domain.grid.Direction
import com.martian.robots.domain.grid.Point

/** A robot's state: where it is on the grid, and its [orientation]: the direction it is facing. */
data class Robot(val location: Point, val orientation: Direction) {

    /** Turns 90 degrees left, staying on the same grid point. */
    fun turnLeft(): Robot = copy(orientation = orientation.anticlockwise())

    /** Turns 90 degrees right, staying on the same grid point. */
    fun turnRight(): Robot = copy(orientation = orientation.clockwise())

    /** Moves one grid point in the direction the robot is facing, keeping the same orientation. */
    fun moveForward(): Robot = copy(location = location.neighbour(towards = orientation))
}
