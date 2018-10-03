package org.team639.robot.commands.auto;

public class Vector {
    public final double x;
    public final double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    /**
     * Returns magnitude of vector.
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns dot product of this vector with argument.
     *
     * @param vec Vector with which to perform dot product.
     */
    public double dot(Vector vec) {
        return x * vec.x + y * vec.y;
    }

    /**
     * Rotate a vector in Cartesian space.
     *
     * @param angle angle in degrees by which to rotate vector counter-clockwise.
     * @return The rotated vector.
     */
    public Vector rotate(double angle) {
        double cosA = Math.cos(angle * (Math.PI / 180.0));
        double sinA = Math.sin(angle * (Math.PI / 180.0));
        double[] out = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        double newX = out[0];
        double newY = out[1];
        return new Vector(newX, newY);
    }

    /**
     * I think this is supposed to make a unit vector with the same angle so that's what I wrote.
     * @return A unit vector at the same angle of this one.
     */
    public Vector normalize() {
        double angle = Math.atan2(y, x);
        return new Vector(Math.cos(angle), Math.sin(angle));
    }

    public Vector scalarMultiply(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }
}
