package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class LEDOneOffSet extends LEDPattern {
    LEDColor[] pattern;
    LEDColor color;
    public LEDOneOffSet(LEDColor color, int length) {
        pattern = new LEDColor[length];
        this.color = color;
    }

    @Override
    public LEDColor[] start() {
        for(int i = 0; i < pattern.length; i++) {
            pattern[i] = color;
        }
        return pattern;
    }

    @Override
    public void stop() {

    }

    @Override
    public LEDColor[] nextPortion() {
        color = new LEDColor(color.getRed()+1, color.getGreen()+1, color.getBlue()+1);
        for(int i = 0; i < pattern.length; i++) {
            pattern[i] = color;
        }
        return pattern;
    }
}
