package org.team639.lib.auto;

import java.util.Optional;

/**
 * A class representing a Position of the robot. Includes XY coordinates and optionally an angle for the robot to face.
 *
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/15/2018.
 * Part of 2018-offseason-experiments.
 */
public class Position {
    public final double x;
    public final double y;
    public final Optional<Double> angle;

    /**
     * Creates a Position with the given xy coordinates.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Position(double x, double y) {
        this(x, y, null);
    }

    /**
     * Creates a Position with the given xy coordinates and the given angle for the robot to face.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param angle The angle the robot is facing.
     */
    public Position(double x, double y, Double angle) {
        this.x = x;
        this.y = y;
        this.angle = Optional.ofNullable(angle);
    }

    /**
     * Creates a copy of the Position.
     * @return A copy of the Position.
     */
    public Position clone() {
        if (angle.isPresent()) return new Position(this.x, this.y, angle.get());
        else return new Position(this.x, this.y);
    }
}
