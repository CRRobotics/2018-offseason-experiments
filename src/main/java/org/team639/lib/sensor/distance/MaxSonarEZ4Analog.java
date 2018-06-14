package org.team639.lib.sensor.distance;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * A MaxSonar EZ4 ultrasonic distance sensor communicating through an analog port.
 */
public class MaxSonarEZ4Analog implements DistanceSensor {

    private AnalogInput input;
    private double lastDistance;

    /**
     * Constructs a new MaxSonarEZ4Analog
     * @param channel The analog channel to listen on.
     */
    public MaxSonarEZ4Analog(int channel) {
        input = new AnalogInput(channel);
    }

    /**
     * Returns the distance registered in inches.
     *
     * @return the distance registered in inches.
     */
    @Override
    public double getDistanceInches() {
        double distance = input.getVoltage() * 512 / 5;
        if (distance > lastDistance) {
            distance = input.getVoltage() * 512 / 5;
        }
        lastDistance = distance;
        return distance;
    }
}