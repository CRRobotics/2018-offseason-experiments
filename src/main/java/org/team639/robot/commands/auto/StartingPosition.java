package org.team639.robot.commands.auto;

/**
 * The three possible starting positions of the robot.
 */
public enum StartingPosition {
    Right(115.5, 19.25),
    Center(4.5, 19.25),
    Left(-115.5, 19.25);

    public final double x;
    public final double y;

    StartingPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
