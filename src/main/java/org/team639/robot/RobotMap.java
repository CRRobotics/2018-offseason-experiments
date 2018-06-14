package org.team639.robot;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

/**
 * Contains references to all of the motors, sensors, pneumatics, etc. Controls access by the rest of the code from a central location.
 */
public class RobotMap {
    private static boolean initialized = false;

//    private static PowerDistributionPanel pdp;

    // Left drive
    private static TalonSRX leftDrive;
    private static IMotorController leftFollower1;
    private static IMotorController leftFollower2;

    // Right drive
    private static TalonSRX rightDrive;
    private static IMotorController rightFollower1;
    private static IMotorController rightFollower2;

    // Drive shifter
    private static Solenoid driveShifter;

    // NAVX
    private static AHRS ahrs;

    private RobotMap() {
    }

    /**
     * Initializes all of the motors, sensors, etc.
     * THIS MUST BE RUN AT THE BEGINNING OF robotInit in Robot.java!!!
     */
    public static void init() {
        if (!initialized) {

            // Left drive
            leftDrive = new TalonSRX(3);
            rightDrive = new TalonSRX(0);

            leftFollower1 = new TalonSRX(4);
            leftFollower2 = new TalonSRX(5);

            rightFollower1 = new TalonSRX(1);
            rightFollower2 = new TalonSRX(2);


            driveShifter = new Solenoid(5);

            // NAVX
            ahrs = new AHRS(SPI.Port.kMXP);

            initialized = true;
        }
    }

    /**
     * Returns the left side Talon.
     *
     * @return The left side Talon.
     */
    public static TalonSRX getLeftDrive() {
        return leftDrive;
    }

    /**
     * Returns the first left side Victor.
     *
     * @return the first left side Victor.
     */
    public static IMotorController getLeftFollower1() {
        return leftFollower1;
    }

    /**
     * Returns the second left side Victor.
     *
     * @return the second left side Victor.
     */
    public static IMotorController getLeftFollower2() {
        return leftFollower2;
    }

    /**
     * Returns the right side Talon.
     *
     * @return The right side Talon.
     */
    public static TalonSRX getRightDrive() {
        return rightDrive;
    }

    /**
     * Returns the first right side Victor.
     *
     * @return The first right side Victor.
     */
    public static IMotorController getRightFollower1() {
        return rightFollower1;
    }

    /**
     * Returns the second right side Victor.
     *
     * @return The second right side Victor.
     */
    public static IMotorController getRightFollower2() {
        return rightFollower2;
    }

    /**
     * Returns the solenoid that controls gear shifting on the drivetrain.
     *
     * @return The solenoid that controls gear shifting on the drivetrain.
     */
    public static Solenoid getDriveShifter() {
        return driveShifter;
    }

    /**
     * Returns the navX gyro.
     *
     * @return The navX gyro.
     */
    public static AHRS getAhrs() {
        return ahrs;
    }
}