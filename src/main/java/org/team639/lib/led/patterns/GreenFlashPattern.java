package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class GreenFlashPattern extends LEDPattern {
    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    private int len;
    private float offset = 0;
    public GreenFlashPattern(int len) {
        this.len = len;
    }

    @Override
    public LEDColor[] start() {
        offset = 0;
        return nextPortion();
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
        LEDColor[] ca = new LEDColor[len];
        for(int i = 0; i < len; i++) {
            LEDColor c = new LEDColor(
                    0,
                    (int)Math.round(Math.pow(Math.max(0, Math.sin(i * 0.2 + offset)), 2)*256),
                    0
            );
            ca[i] = c;
        }
        offset = (offset + 1.5f) % (float)(2*Math.PI);
        //offset %= 2*Math.PI;
        return ca;
    }
}
