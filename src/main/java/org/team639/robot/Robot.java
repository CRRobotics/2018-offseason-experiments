package org.team639.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team639.lib.auto.AutoCommand;
import org.team639.robot.commands.auto.*;
import org.team639.robot.commands.drive.DriveMode;
import org.team639.robot.subsystems.BlueLEDs;
import org.team639.robot.subsystems.DriveTrain;
import org.team639.robot.subsystems.GreenLEDs;
import org.team639.robot.subsystems.RedLEDs;

import static org.team639.robot.Constants.DriveTrain.HIGH_DRIVE_D;
import static org.team639.robot.Constants.DriveTrain.HIGH_DRIVE_I;
import static org.team639.robot.Constants.DriveTrain.HIGH_DRIVE_P;

/**
 * The main robot class.
 */
public class Robot extends TimedRobot {

    private static double rMax = 0;
    private static double lMax = 0;
    private static double liftMax = 0;

    private Command auto; // The auto period (very important)

    // Subsystems
    private static DriveTrain driveTrain;
    private static RedLEDs redLEDs;
    private static BlueLEDs blueLEDs;
    private static GreenLEDs greenLEDs;

    // Driver options
    private static SendableChooser<DriveMode> driveMode;
    private static SendableChooser<ControlMode> driveTalonControlMode;
    private static SendableChooser<StartingPosition> startingPosition;
    private static SendableChooser<AutoCommand> autoSelector;

    /**
     * Returns a reference to the robot's drivetrain.
     * @return a reference to the robot's drivetrain.
     */
    public static DriveTrain getDriveTrain() {
        return driveTrain;
    }

    public static RedLEDs getRedLEDs() {
        return redLEDs;
    }

    public static BlueLEDs getBlueLEDs() {
        return blueLEDs;
    }

    public static GreenLEDs getGreenLEDs() {
        return greenLEDs;
    }

    /**
     * Returns the drive mode selected by the driver on shuffleboard.
     * @return The drive mode selected by the driver on shuffleboard.
     */
    public static DriveMode getDriveMode() {
        return driveMode.getSelected();
    }

    public static ControlMode getDriveTalonControlMode() {
        return driveTalonControlMode.getSelected();
    }

    /**
     * Returns the starting position of the robot, specified on shuffleboard by the drive team.
     * @return The starting position of the robot, specified on shuffleboard by the drive team.
     */
    public static StartingPosition getStartingPosition() {
        return startingPosition.getSelected();
    }


    /**
     * Robot-wide initialization code should go here.
     * <p>
     * <p>Users should override this method for default Robot-wide initialization which will be called
     * when the robot is first powered on. It will be called exactly one time.
     * <p>
     * <p>Warning: the Driver Station "Robot Code" light and FMS "Robot Ready" indicators will be off
     * until RobotInit() exits. Code in RobotInit() that waits for enable will cause the robot to
     * never indicate that the code is ready, causing the robot to be bypassed in a match.
     */
    @Override
    public void robotInit() {
        RobotMap.init(); // Initialize all sensors, motors, etc.

        SmartDashboard.putNumber("drive p", HIGH_DRIVE_P);
        SmartDashboard.putNumber("drive i", HIGH_DRIVE_I);
        SmartDashboard.putNumber("drive d", HIGH_DRIVE_D);

        // Subsystem initializations
        driveTrain = new DriveTrain();
        redLEDs = new RedLEDs();
        blueLEDs = new BlueLEDs();
        greenLEDs = new GreenLEDs();


        // Driver options init
        driveMode = new SendableChooser<>();
        driveMode.addDefault("2 Joystick Arcade Right", DriveMode.Arcade2JoystickRightDrive);
        driveMode.addObject("1 Joystick Arcade", DriveMode.Arcade1Joystick);
        driveMode.addObject("Tank", DriveMode.Tank);
        driveMode.addObject("Field Oriented 1 joystick", DriveMode.Field1Joystick);
        driveMode.addObject("Field Oriented 2 joysticks", DriveMode.Field2Joystick);
        driveMode.addObject("2 Joystick Arcade Left", DriveMode.Arcade2JoystickLeftDrive);
        SmartDashboard.putData("Drive Mode", driveMode);

        driveTalonControlMode = new SendableChooser<>();
        driveTalonControlMode.addDefault("Closed loop", ControlMode.Velocity);
        driveTalonControlMode.addObject("Open loop", ControlMode.PercentOutput);
        SmartDashboard.putData("Control mode", driveTalonControlMode);

        startingPosition = new SendableChooser<>();
        startingPosition.addDefault("Center", StartingPosition.Center);
        startingPosition.addObject("Left", StartingPosition.Left);
        startingPosition.addObject("Right", StartingPosition.Right);
        SmartDashboard.putData("Starting position", startingPosition);

        SmartDashboard.putNumber("Auto delay", 0);
        autoSelector = new SendableChooser<>();
        autoSelector.addDefault("Example (Test)", new ExampleAuto());
        SmartDashboard.putData("Auto selector", autoSelector);

        OI.mapButtons(); // Map all of the buttons on the controller(s)
    }

    /**
     * Initialization code for disabled mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters disabled mode.
     */
    @Override
    public void disabledInit() {
    }

    /**
     * Initialization code for autonomous mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        auto = autoSelector.getSelected();
        auto.start();
        System.out.println("Auto started");
//        auto.start();
    }

    /**
     * Initialization code for teleop mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters teleop mode.
     */
    @Override
    public void teleopInit() {
        driveTrain.setNeutralMode(NeutralMode.Brake);

        if (auto != null) auto.cancel(); // Stop the auto
    }

    /**
     * Initialization code for test mode should go here.
     * <p>
     * <p>Users should override this method for initialization code which will be called each time the
     * robot enters test mode.
     */
    @Override
    public void testInit() {
        super.testInit();
    }

    /**
     * Periodic code for all robot modes should go here.
     */
    @Override
    public void robotPeriodic() {

        SmartDashboard.putString("Selected auto mode", autoSelector.getSelected().getClass().getSimpleName());

        SmartDashboard.putBoolean("drivetrain encoders", driveTrain.encodersPresent());
        SmartDashboard.putNumber("Left speed", driveTrain.getLeftEncVelocity());
        SmartDashboard.putNumber("Right speed", driveTrain.getRightEncVelocity());

        SmartDashboard.putNumber("navx yaw", driveTrain.getRobotYaw());

//        SmartDashboard.putNumber("left enc", driveTrain.getLeftEncPos());
//        SmartDashboard.putNumber("right enc", driveTrain.getRightEncPos());

        double r = driveTrain.getRightEncVelocity();
        double l = driveTrain.getLeftEncVelocity();
        if (r > rMax) {
            rMax = r;
            SmartDashboard.putNumber("r max", rMax);
        }

        if (l > lMax) {
            lMax = l;
            SmartDashboard.putNumber("l max", lMax);
        }
    }

    /**
     * Periodic code for disabled mode should go here.
     */
    @Override
    public void disabledPeriodic() {
    }

    /**
     * Periodic code for autonomous mode should go here.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Periodic code for teleop mode should go here.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Periodic code for test mode should go here.
     */
    @Override
    public void testPeriodic() {
        super.testPeriodic();
    }
}
