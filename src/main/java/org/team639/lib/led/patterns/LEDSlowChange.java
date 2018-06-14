package org.team639.lib.led.patterns;

import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class LEDSlowChange extends LEDPattern {

    private LEDColor color;
    private LEDColor[] shifting;
    private int length;
    private int changeTime;
    public LEDSlowChange(int length, LEDColor color) {
        //this.color = changeColor;
        this.length = length;
        shifting = new LEDColor[length];
        this.color = color;
    }



    @Override
    public LEDColor[] start() {
        for(int i = 0; i < length; i++) {
            shifting[i] = color;
        }
        return shifting;
    }

    @Override
    public void stop() {

    }

    @Override
    public LEDColor[] nextPortion() {

        color.setBlue((color.getBlue()+1 % 255));
        for(int i = 0; i < length; i++) {
            shifting[i] = color;
        }
        return shifting;
    }
}
