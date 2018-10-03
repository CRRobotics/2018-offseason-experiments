package org.team639.robot.commands.auto;

public class Waypoint {
    private final double x;
    private final double y;

    public Waypoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Vector minus(Waypoint other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    public Waypoint plus(Vector vec) {
        return new Waypoint(x + vec.x, y + vec.y);
    }
}
