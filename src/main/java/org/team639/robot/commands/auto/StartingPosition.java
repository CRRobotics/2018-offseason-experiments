package org.team639.robot.commands.auto;

import org.team639.lib.auto.Position;

/**
 * The three possible starting positions of the robot.
 */
public enum StartingPosition {
    Right(new Position(115.5, 19.25, 90d)),
    Center(new Position(4.5, 19.25, 90d)),
    Left(new Position(-115.5, 19.25, 90d));

    public final Position position;

    StartingPosition(Position pos) {
        this.position = pos;
    }
}
