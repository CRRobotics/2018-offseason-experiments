package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

/**
 * An LED pattern that blinks periodically.
 */
public class LEDBlink extends LEDPattern {
    private LEDColor color;
    private LEDColor[] on;
    private LEDColor[] off;

    private int length;
    private int onTime;
    private int offTime;
    private long lastChangeTime;
    private boolean currentlyOn;

    /**
     * Creates a new LEDBlink with given attributes.
     * @param color The color to blink.
     * @param length The length of the LED strip.
     * @param changeTime The time between changing state.
     */
    public LEDBlink(LEDColor color, int length, int changeTime) {
        this(color, length, changeTime, changeTime);
    }

    /**
     * Creates a new LEDBlink with given attributes.
     * @param color The color to blink.
     * @param length The length of the LED strip.
     * @param onTime The time the LED strip is on each cycle.
     * @param offTime The time the LED strip is off each cycle.
     */
    public LEDBlink(LEDColor color, int length, int onTime, int offTime) {
        this.color = color;
        this.length = length;
        this.onTime = onTime;
        this.offTime = offTime;
    }

    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    @Override
    public LEDColor[] start() {
        on = new LEDColor[length];
        off = new LEDColor[length];

        LEDColor offColor = new LEDColor(0, 0, 0);

        for (int i = 0; i < length; i++) {
            on[i] = color;
            off[i] = offColor;
        }

        currentlyOn = true;
        lastChangeTime = System.currentTimeMillis();
        return on;
    }

    /**
     * Called whenever the pattern becomes inactive.
     */
    @Override
    public void stop() {
        on = null;
        off = null;
    }

    /**
     * Returns an array of colors to be written to the LEDs.
     *
     * @return An array of colors to be written to the LEDs.
     */
    @Override
    public LEDColor[] nextPortion() {
        if (currentlyOn && System.currentTimeMillis() - lastChangeTime >= onTime) {
            currentlyOn = false;
            lastChangeTime = System.currentTimeMillis();
            return off;
        } else if (!currentlyOn && System.currentTimeMillis() - lastChangeTime >= offTime) {
            currentlyOn = true;
            lastChangeTime = System.currentTimeMillis();
            return on;
        } else {
            return new LEDColor[0];
        }
    }
}
