package org.team639.robot.commands.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.math.AngleMath;
import org.team639.lib.math.PID;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import static org.team639.robot.Constants.Auto.*;

/**
 * Turns the robot to a specified angle.
 * Constants for this routine are in the static Constants.Auto class and are prefixed with TTA_
 */
public class AutoTurnToAngle extends Command {

    private DriveTrain driveTrain;

    private double angle;
    private boolean done;

    private double lSpeed;
    private double rSpeed;

    private double startSlow;
    private double minSpeed;

    private PID pid;

    public AutoTurnToAngle(double angle) {
        super("AutoTurnToAngle");
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
        this.angle = angle % 360;
    }

    //anthony is a pretty cool guy
    protected void initialize() {
        done = false;

        driveTrain.setCurrentGear(DriveTrain.DriveGear.High);
//        double p = SmartDashboard.getNumber("drive p", TTA_P);
//        double i = SmartDashboard.getNumber("drive i", TTA_I);
//        double d = SmartDashboard.getNumber("drive d", TTA_D);
//        double rate = SmartDashboard.getNumber("rate", TTA_RATE);
//        double tolerance = TTA_TOLERANCE; // SmartDashboard.getNumber("tolerance", 2);
//        double min = SmartDashboard.getNumber("min", TTA_MIN);
//        double max = SmartDashboard.getNumber("max", TTA_MAX);
//        double iCap = SmartDashboard.getNumber("iCap", TTA_I_CAP);
//        pid = new PID(p, i, d, min, max, rate, tolerance, iCap);
        pid = new PID(TTA_P, TTA_I, TTA_D, TTA_MIN, TTA_MAX, TTA_RATE, TTA_TOLERANCE, TTA_I_CAP);

        driveTrain.setSpeedsPercent(0, 0);
        driveTrain.setCurrentControlMode(ControlMode.Velocity);
    }

    protected void execute() {
        double error = AngleMath.shortestAngle(driveTrain.getRobotYaw(), angle);
//        SmartDashboard.putNumber("angle error", error);
        double val = pid.compute(error);
        done = (val == 0);
        driveTrain.setSpeedsPercent(-1 * val, val);
    }

    @Override
    protected boolean isFinished() {
        return Math.abs(AngleMath.shortestAngle(driveTrain.getRobotYaw(), angle)) < TTA_TOLERANCE;
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
