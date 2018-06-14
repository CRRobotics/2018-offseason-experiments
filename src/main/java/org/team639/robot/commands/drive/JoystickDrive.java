package org.team639.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team639.lib.controls.LogitechF310;
import org.team639.lib.math.AngleMath;
import org.team639.lib.math.PID;
import org.team639.robot.Constants;
import org.team639.robot.OI;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import static org.team639.robot.Constants.DriveTrain.*;

/**
 * Controls DriveTrain with Joysticks.
 * Default DriveTrain command.
 */
public class JoystickDrive extends Command {
    private DriveTrain driveTrain = Robot.getDriveTrain();

    private PID turnPID;

    private double lastSetpointSpeed = 0;
    private double lastSetpointTurning = 0;

    private double lastSetpointRight = 0;
    private double lastSetpointLeft = 0;

    public JoystickDrive() {
        super("JoystickDrive");
        requires(driveTrain);
    }

    protected void initialize() {
        driveTrain.setCurrentControlMode(ControlMode.PercentOutput);
        // Field oriented drive turning PID constants from Constants.DriveTrain, prefixed with FOT_
        double p = FOT_P;
        double i = FOT_I;
        double d = FOT_D;
        double rate = FOT_RATE;
        double tolerance = FOT_TOLERANCE;
        double min = FOT_MIN;
        double max = FOT_MAX;
        double iCap = FOT_I_CAP;
//        double p = SmartDashboard.getNumber("drive p", Constants.DriveTrain.HIGH_DRIVE_P);
//        double i = SmartDashboard.getNumber("drive i", Constants.DriveTrain.HIGH_DRIVE_I);
//        double d = SmartDashboard.getNumber("drive d", Constants.DriveTrain.HIGH_DRIVE_I);
//        double rate = SmartDashboard.getNumber("rate", 0.1);
//        double tolerance = SmartDashboard.getNumber("tolerance", 2);
//        double min = SmartDashboard.getNumber("min", 0.2);
//        double max = SmartDashboard.getNumber("max", 0.5);
        turnPID = new PID(p, i, d, min, max, rate, tolerance, iCap);
//        driveTrain.setRampRate(0);//.5);
        //FIXME: doesn't necessarily resend constants
        driveTrain.setCurrentGear(driveTrain.getCurrentGear()); // Resets to default pid values for current gear.
    }

    /**
     * Called repeatedly while the command is running.
     */
    protected void execute() {
        if (!driveTrain.encodersPresent()) driveTrain.setCurrentControlMode(ControlMode.PercentOutput); // TODO: Time limit to wait before switching
        else driveTrain.setCurrentControlMode(Robot.getDriveTalonControlMode());

        double p = SmartDashboard.getNumber("drive p", Constants.DriveTrain.HIGH_DRIVE_P);
        double i = SmartDashboard.getNumber("drive i", Constants.DriveTrain.HIGH_DRIVE_I);
        double d = SmartDashboard.getNumber("drive d", Constants.DriveTrain.HIGH_DRIVE_I);

        driveTrain.setPID(p, i, d, HIGH_DRIVE_F);

        DriveMode mode;
        double x;
        double y;
        double speed;
        double angle;

        double scale = 1 - 0.8 * OI.drive.getControllerAxis(LogitechF310.ControllerAxis.RightTrigger);
        if (scale < 0.2) scale = 0.2;
        mode = Robot.getDriveMode();
        switch (mode) {
            case Tank:
                tankDrive(OI.drive.getLeftStickY() * scale, OI.drive.getRightStickY() * scale);
                break;
            case Arcade1Joystick:
                arcadeDrive(OI.drive.getRightStickY() * scale, OI.drive.getRightStickX() * scale);
                break;
            case Arcade2JoystickLeftDrive:
                arcadeDrive(OI.drive.getLeftStickY() * scale, OI.drive.getRightStickX() * scale);
                break;
            case Arcade2JoystickRightDrive:
                arcadeDrive(OI.drive.getRightStickY() * scale, OI.drive.getLeftStickX() * scale);
                break;
            case Field1Joystick:
                x = OI.drive.getRightStickX();
                y = OI.drive.getRightStickY();
                angle = Math.abs(x) >= Constants.JOYSTICK_DEADZONE || Math.abs(y) >= Constants.JOYSTICK_DEADZONE ? OI.drive.getRightDriveAngle() : 500;
                speed = Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2));
                fieldOrientedDrive(angle, speed, 1);
                break;
            case Field2Joystick:
                x = OI.drive.getLeftStickX();
                y = OI.drive.getLeftStickY();
                angle = Math.abs(x) >= Constants.JOYSTICK_DEADZONE || Math.abs(y) >= Constants.JOYSTICK_DEADZONE ? OI.drive.getLeftDriveAngle() : 500;
                speed = Math.sqrt(Math.pow(y, 2) + Math.pow(x, 2));
                fieldOrientedDrive(angle, OI.drive.getRightStickY() * scale, speed);
                break;
        }
    }

    /**
     * Returns whether or not the command is finished.
     * @return whether or not the command is finished.
     */
    @Override
    protected boolean isFinished() {
        return false;
    }

    /**
     * Drives the robot in a field oriented manner
     * @param angle The angle to drive in. Angles greater than 360 represent no turning.
     * @param moveSpeed The speed to drive at.
     */
    private void fieldOrientedDrive(double angle, double moveSpeed, double turnSpeed) {
        double output;
        if (moveSpeed > 1) moveSpeed = 1;
        if (turnSpeed > 1) turnSpeed = 1;
        if (angle < 360) {
            double error = AngleMath.shortestAngle(driveTrain.getRobotYaw(), angle);
            output = turnPID.compute(error);
        } else {
            output = 0;
        }
//        System.out.printf("error: %f, output: %f, angle: %f, speed: %f, l: %f, R: %f\n", AngleMath.shortestAngle(driveTrain.getRobotYaw(), angle), output, angle, moveSpeed, moveSpeed / 2 - output * turnSpeed, moveSpeed / 2 + output * turnSpeed);
        driveTrain.setSpeedsPercent(moveSpeed / 2 - output * turnSpeed, moveSpeed / 2 + output * turnSpeed);
    }

    /**
     * Performs arcade driving
     * @param speed The magnitude of speed from -1 to 1
     * @param turning The turning magnitude from -1 to 1
     */
    public void arcadeDrive(double speed, double turning) {
        speed = speed * 2 / 3; //Math.abs(speed);//speed * 2 / 3;
//        turning /= 3; //Math.abs(turning);
        double turnMultiplier = 1 - speed;
        if (turnMultiplier < 1d / 3d) turnMultiplier = 1d / 3d;
        if (turnMultiplier > 2d / 3d) turnMultiplier = 2d / 3d;
        turning = turning * turnMultiplier;
        SmartDashboard.putNumber("turning", turning);
        double rate = SmartDashboard.getNumber("rrate", HIGH_ARCADE_RATE); //driveTrain.getCurrentGear() == DriveTrain.DriveGear.High ? HIGH_ARCADE_RATE : LOW_ARCADE_RATE;

//        if (Math.abs(speed - lastSetpointSpeed) > rate) {
//            speed = speed < lastSetpointSpeed ? lastSetpointSpeed - rate : lastSetpointSpeed + rate;
//        }
//        if (Math.abs(turning - lastSetpointTurning) > rate) {
//            turning = turning < lastSetpointTurning ? lastSetpointTurning - rate : lastSetpointTurning + rate;
//        }
        lastSetpointSpeed = speed;
        lastSetpointTurning = turning;

        if (speed < -.01) turning *= -1;

        driveTrain.setSpeedsPercent(speed + turning, speed - turning);
    }


    /**
     * Takes two speed values from -1 to 1 and uses them for tank driving
     * @param lSpeed The value for the left side
     * @param rSpeed The value for the right side
     */
    public void tankDrive(double lSpeed, double rSpeed) {
//        lSpeed /= 2;
//        rSpeed /= 2;
        double rate = driveTrain.getCurrentGear() == DriveTrain.DriveGear.High ? HIGH_ARCADE_RATE : LOW_ARCADE_RATE;


//        if (Math.abs(lSpeed - lastSetpointLeft) > rate) {
//            lSpeed = lSpeed < lastSetpointLeft ? lastSetpointLeft - rate : lastSetpointLeft + rate;
//        }
//        if (Math.abs(rSpeed - lastSetpointRight) > rate) {
//            rSpeed = rSpeed < lastSetpointRight ? lastSetpointRight - rate : lastSetpointRight + rate;
//        }

        lastSetpointRight = rSpeed;
        lastSetpointLeft = lSpeed;
        driveTrain.setSpeedsPercent(lSpeed, rSpeed);
    }
}
