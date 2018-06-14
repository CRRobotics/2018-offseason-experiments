package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

/**
 * A solid red LED pattern.
 */
public class LEDRed extends LEDPattern {
    private int length;
    private int brightness;

    /**
     * Creates a new LEDRed with given length.
     * @param length The length of the led strip.
     */
    public LEDRed(int length) {
        this(length, 255);
    }

    /**
     * Creates a new LEDRed with given length and brightness.
     * @param length The length of the led strip.
     * @param brightness The brightness of the pattern from 0-255.
     */
    public LEDRed(int length, int brightness) {
        this.length = length;
        this.brightness = brightness;
        if (this.brightness < 0) brightness = 0;
        if (this.brightness > 255) brightness = 255;
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
            pattern[i] = new LEDColor(brightness, 0, 0);
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
