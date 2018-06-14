package org.team639.lib.led.patterns;

import edu.wpi.first.wpilibj.RobotController;
import org.team639.lib.led.LEDColor;
import org.team639.lib.led.LEDPattern;

public class LEDBatteryPercent extends LEDPattern {

    private LEDColor[] pattern;
    private double percent;
    public LEDBatteryPercent(int length) {
        pattern = new LEDColor[length];
        percent = RobotController.getBatteryVoltage() / 13;
    }

    @Override
    public LEDColor[] start() {
        percent = (RobotController.getBatteryVoltage()) / 13;
        percent = percent - ((1 - percent) / 3);
        double percentRed;
        double percentGreen;
        if(percent > .50) {
            percentRed = percent / 4;
            percentGreen = percent;
        } else {
            percentRed = percent;
            percentGreen = percent / 6;
        }
        for(int i = 0; i < round(percent*pattern.length); i++) {
            pattern[i] = new LEDColor(round(255*percentRed), round(percentGreen*255), 0);
        }
        for(int i = round(percent*pattern.length); i < pattern.length; i++){
            pattern[i] = new LEDColor(0,0,0);
        }
        if(RobotController.isBrownedOut()) {
            for(int i = 0; i < pattern.length; i++){
                pattern[i] = new LEDColor(255, 0, 0);
            }
        }
        return pattern;
    }

    @Override
    public void stop() {

    }

    @Override
    public LEDColor[] nextPortion() {
        pattern = start();
        return pattern;
    }

    private int round(double d) {
        if((int)(d + 0.5) > (int) d) return (int)d + 1;
        else return (int)d;
    }
}
