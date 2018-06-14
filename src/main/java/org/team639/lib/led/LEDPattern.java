package org.team639.lib.led;

/**
 * An abstract class representing an LED pattern
 */
public abstract class LEDPattern {

    /**
     * Called whenever the pattern becomes active.
     * @return Initial values to be written to the led strip.
     */
    public abstract LEDColor[] start();

    /**
     * Called whenever the pattern becomes inactive.
     */
    public abstract void stop();

    /**
     * Returns an array of colors to be written to the LEDs.
     * @return An array of colors to be written to the LEDs.
     */
    public abstract LEDColor[] nextPortion();
}
