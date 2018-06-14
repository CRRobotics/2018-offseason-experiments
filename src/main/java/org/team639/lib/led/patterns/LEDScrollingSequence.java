package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class LEDScrollingSequence extends LEDPattern {
    private LEDColor[] colors;
    private int length;
    private int currentPosition;
    private int changeTimeMS;
    private long lastChangeTime;

    private LEDColor[] sequence;

    public LEDScrollingSequence(LEDColor[] colors, int length, int changeTimeMS) {
        this.colors = colors;
        this.length = length;
        this.changeTimeMS = changeTimeMS;
    }

    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    @Override
    public LEDColor[] start() {
        sequence = new LEDColor[length];
        currentPosition = 0;
        for (int i = 0; i < length; i++) {
            sequence[i] = colors[currentPosition];
            currentPosition++;
            currentPosition %= colors.length;
        }
        lastChangeTime = System.currentTimeMillis();
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
        if (System.currentTimeMillis() - lastChangeTime >= changeTimeMS) {
            for (int i = length - 1; i > 0; i--) {
                sequence[i] = sequence[i - 1];
            }
            sequence[0] = colors[currentPosition];
            lastChangeTime = System.currentTimeMillis();
            currentPosition++;
            currentPosition %= colors.length;
            return sequence;
        } else {
            return new LEDColor[0];
        }
    }
}
