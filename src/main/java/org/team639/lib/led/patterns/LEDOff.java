package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;

/**
 * A completely off LED pattern.
 */
public class LEDOff extends LEDSolid {
    /**
     * Creates a new LEDOff with given length.
     * @param length The length of the led strip.
     */
    public LEDOff(int length) {
        super(new LEDColor(0, 0, 0), length);
    }
}
