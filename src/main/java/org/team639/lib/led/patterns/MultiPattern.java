package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class MultiPattern extends LEDPattern {
    int[] lengths;
    LEDColor[] colors;
    LEDPattern[] patterns;
    public MultiPattern(int segments) {
        this.lengths = new int[segments];
        int sum = 0;
        for(int x : lengths) {
            sum += x;
        }
        colors = new LEDColor[sum];
        patterns = new LEDPattern[lengths.length];
        for(int i = 0; i < sum; i++) {
            colors[i] = new LEDColor(0, 0, 0);
        }
    }

    public void setSegment(int s, LEDPattern p, int length) {
        patterns[s] = p;
        lengths[s] = length;
    }

    @Override
    public LEDColor[] start() {
        for(int i = 0, offset = 0; i < lengths.length; offset += lengths[i], i++) {
            int l = lengths[i];
            LEDPattern p = patterns[i];
            if(p != null) {
                LEDColor[] cs = p.start();
                for (int j = 0, o = offset; j < l; j++, o++) {
                    colors[o] = cs[j];
                }
            }
        }
        return colors;
    }

    /**
     * Called whenever the pattern becomes inactive.
     */
    @Override
    public void stop() {
        for(LEDPattern p : patterns) p.stop();
    }

    /**
     * Returns an array of colors to be written to the LEDs.
     *
     * @return An array of colors to be written to the LEDs.
     */
    @Override
    public LEDColor[] nextPortion() {
        for(int i = 0, offset = 0; i < lengths.length; offset += lengths[i], i++) {
            int l = lengths[i];
            LEDPattern p = patterns[i];
            if(p != null) {
                LEDColor[] cs = p.nextPortion();
                for (int j = 0, o = offset; j < l; j++, o++) {
                    colors[o] = cs[j];
                }
            }
        }
        return colors;
    }
}
