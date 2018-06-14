package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class SinePattern extends LEDPattern {
    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    private int len;
    private float offset = 0;
    public SinePattern(int len) {
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
                    (int)(Math.sin(i * 0.1 + offset)*127 + 127),
                    (int)(Math.sin(i * 0.1 + Math.PI * 2 / 3. + offset)*127 + 127),
                    (int)(Math.sin(i * 0.1 + Math.PI * 4 / 3. + offset)*127 + 127)
            );
            ca[i] = c;
        }
        offset += .05;
        return ca;
    }
}
