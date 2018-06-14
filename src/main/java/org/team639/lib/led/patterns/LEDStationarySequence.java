package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class LEDStationarySequence extends LEDPattern {
    private LEDColor[] colors;
    private int length;

    public LEDStationarySequence(LEDColor[] colors, int length) {
        this.colors = colors;
        this.length = length;
    }

    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    @Override
    public LEDColor[] start() {
        LEDColor[] sequence = new LEDColor[length];
        int j = 0;
        for (int i = 0; i < length; i++) {
            sequence[i] = colors[j];
            j++;
            j %= colors.length;
        }
        return sequence;
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
