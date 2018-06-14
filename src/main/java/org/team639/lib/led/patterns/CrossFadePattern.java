package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class CrossFadePattern extends LEDPattern {

    private LEDPattern patternA;
    private LEDPattern patternB;
    private int fadeTicks;

    private int cTicks = 0;

    public CrossFadePattern(LEDPattern a, LEDPattern b, int fadeTicks) {
        patternA = a;
        patternB = b;
        this.fadeTicks = fadeTicks;
    }

    private int mix(int a, int b, float percent) {
        return (int)(a * (1-percent) + b * percent);
    }

    private LEDColor mix(LEDColor a, LEDColor b, float percent) {
        return new LEDColor(mix(a.getRed(), b.getRed(), percent), mix(a.getGreen(), b.getGreen(), percent), mix(a.getBlue(), b.getBlue(), percent));
    }

    private LEDColor[] mix(LEDColor[] a, LEDColor[] b, float percent) {
        if(a.length != b.length) return a;
        LEDColor[] o = new LEDColor[a.length];
        for(int i = 0; i < o.length; i++) {
            o[i] = mix(a[i], b[i], percent);
        }
        return o;
    }

    /**
     * Called whenever the pattern becomes active.
     *
     * @return Initial values to be written to the led strip.
     */
    @Override
    public LEDColor[] start() {
        cTicks = 0;
        patternB.start();
        return patternA.start();
    }

    /**
     * Called whenever the pattern becomes inactive.
     */
    @Override
    public void stop() {
        patternA.stop();
        patternB.stop();
    }

    /**
     * Returns an array of colors to be written to the LEDs.
     *
     * @return An array of colors to be written to the LEDs.
     */
    @Override
    public LEDColor[] nextPortion() {
        cTicks++;
        return mix(patternA.nextPortion(), patternB.nextPortion(), cTicks / (float)fadeTicks);
    }
}
