package org.team639.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import org.team639.robot.RobotMap;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/20/2018.
 * Part of 2018-offseason-experiments.
 */
public class BlueLEDs extends LEDSubsystem {
    private DigitalOutput led1;
    private DigitalOutput led2;

    public BlueLEDs() {
        led1 = RobotMap.getBlueLED1();
        led2 = RobotMap.getBlueLED2();

        led1.set(true);
        led2.set(true);
    }


    public void setLed1(boolean on) {
        led1.set(!on);
    }

    public void setLed2(boolean on) {
        led2.set(!on);
    }

    public boolean getLed1() {
        return !led1.get();
    }

    public boolean getLed2() {
        return !led2.get();
    }
}
