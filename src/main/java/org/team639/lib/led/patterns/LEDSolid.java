package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

/**
 * A solid color LED pattern.
 */
public class LEDSolid extends LEDPattern {
    private int length;
    private LEDColor color;

    /**
     * Creates a new LEDSolid with given length and color.
     * @param color The color for the led strip.
     * @param length The length of the led strip.
     */
    public LEDSolid(LEDColor color, int length) {
        this.length = length;
        this.color = color;
    }

    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    @Override
    public LEDColor[] start() {
        LEDColor[] pattern = new LEDColor[length];
        for (int i = 0; i < length; i++) {
            pattern[i] = color;
        }
        return pattern;
    }

    /**
     * Called whenever the pattern becomes inactive.
     */
    @Override
    public void stop() {

    }

    /**
     * Returns an array of colors to be written to the LEDs.
     *
     * @return An array of colors to be written to the LEDs.
     */
    @Override
    public LEDColor[] nextPortion() {
        return new LEDColor[0];
    }
}
