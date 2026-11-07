package com.martian.robots.application

import com.martian.robots.domain.mars.Mars
import com.martian.robots.domain.mars.Scent
import com.martian.robots.domain.mission.RobotMission
import com.martian.robots.domain.mission.RobotMission.Outcome.Lost

/**
 * The use case for exploring Mars: robots are sent out on [mars] one at a time, following their
 * instructions from Earth, and each one's outcome is returned in the same order as [missions].
 *
 * When a robot is lost, a scent is left at its last position before the next mission starts, so
 * that later robots in the same exploration are not lost from the same point.
 */
fun exploreMars(mars: Mars, missions: List<RobotMission>): List<RobotMission.Outcome> {
    var current = mars
    return missions.map { mission ->
        mission.run(mars = current).also { outcome ->
            if (outcome is Lost) {
                current = current.leaveScent(scent = Scent(location = outcome.robot.location))
            }
        }
    }
}
