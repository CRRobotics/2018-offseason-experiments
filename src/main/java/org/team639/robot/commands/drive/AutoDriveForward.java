package org.team639.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.math.AngleMath;
import org.team639.lib.math.PID;
import org.team639.robot.Constants;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import static org.team639.robot.Constants.Auto.*;
import static org.team639.robot.Constants.DriveTrain.*;

/**
 * Command to autonomously drive forward a specified distance
 */
public class AutoDriveForward extends Command {
    private DriveTrain driveTrain = Robot.getDriveTrain();

    private boolean done = false;

    private double targetDistance;
    private int targetTicks;
    private double lTickDiff;
    private double rTickDiff;
    private double targetLeft;
    private double targetRight;

    private double angle;
    private boolean useAbsoluteAngle = false;

    private PID pid;
    private PID turnPID;

    public AutoDriveForward(double distance) {
        super("AutoDriveForward");
        requires(driveTrain);

        targetDistance = distance;
        targetTicks = (int)(targetDistance * Constants.DriveTrain.TICKS_PER_INCH);

        this.useAbsoluteAngle = false;
    }

    public AutoDriveForward(double distance, double angle) {
        super("AutoDriveForward");
        requires(driveTrain);

        targetDistance = distance;
        targetTicks = (int)(targetDistance * Constants.DriveTrain.TICKS_PER_INCH);

        this.useAbsoluteAngle = true;
        this.angle = angle % 360;
    }

    protected void initialize() {
        done = false;
        if (!useAbsoluteAngle) angle = driveTrain.getRobotAngle();

        targetLeft = driveTrain.getLeftEncPos() + targetTicks;
        targetRight = driveTrain.getRightEncPos() + targetTicks;

//        SmartDashboard.putNumber("startError", targetLeft - driveTrain.getLeftEncPos());

        driveTrain.setSpeedsPercent(0, 0);
        driveTrain.setCurrentControlMode(ControlMode.Velocity);
        // Dominic "DJ" Towns was here.
        driveTrain.setCurrentGear(DriveTrain.DriveGear.High);

//        double p = SmartDashboard.getNumber("drive p", ADF_P);
//        double i = SmartDashboard.getNumber("drive i", ADF_I);
//        double d = SmartDashboard.getNumber("drive d", ADF_D);
//        double rate = SmartDashboard.getNumber("rate", ADF_RATE);
//        double tolerance = SmartDashboard.getNumber("tolerance", ADF_TOLERANCE);
//        double min = SmartDashboard.getNumber("min", ADF_MIN);
//        double max = SmartDashboard.getNumber("max", ADF_MAX);
//        pid  = new PID(p, i, d, min, max, rate, tolerance, ADF_I_CAP);
        pid = new PID(ADF_P, ADF_I, ADF_D, ADF_MIN, ADF_MAX, ADF_RATE, ADF_TOLERANCE, ADF_I_CAP);
//
        turnPID = new PID(AC_P, AC_I, AC_D, AC_MIN, AC_MAX, AC_RATE, AC_TOLERANCE, AC_I_CAP);
    }

    protected void execute() {
        System.out.println("Driving forward");
        lTickDiff = targetLeft - driveTrain.getLeftEncPos();
        rTickDiff = targetRight - driveTrain.getRightEncPos();

        double val = pid.compute((lTickDiff + rTickDiff)/2);
        double error = AngleMath.shortestAngle(driveTrain.getRobotAngle(), angle);
        double output = turnPID.compute(error);
        driveTrain.setSpeedsPercent(val - output, val + output);
        done = (val == 0);
    }

    @Override
    protected boolean isFinished() {
        return done;
    }

    @Override
    protected void interrupted() {
        end();
    }

    @Override
    protected void end() {
        driveTrain.setSpeedsPercent(0, 0);
    }
}
