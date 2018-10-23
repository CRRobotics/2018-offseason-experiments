package org.team639.robot.commands.auto;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import static org.team639.robot.Constants.DriveTrain.HIGH_SPEED_RANGE;
import static org.team639.robot.Constants.DriveTrain.WHEEL_DIAMETER_INCHES;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 10/23/2018.
 * Part of 2018-offseason-experiments.
 */
public class PFFolower extends Command {

    private DriveTrain driveTrain;

    private Trajectory trajectory;
    private TankModifier modifier;

    private EncoderFollower left;
    private EncoderFollower right;

    private boolean done = false;

    public PFFolower(Trajectory trajectory) {
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
        this.trajectory = trajectory;
        this.modifier = new TankModifier(trajectory).modify(0.5); //TODO: CHANGE
        left = new EncoderFollower(modifier.getLeftTrajectory());
        right = new EncoderFollower(modifier.getRightTrajectory());
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        driveTrain.setCurrentControlMode(ControlMode.Velocity);

        done = false;

        left.reset();
        right.reset();

        left.configureEncoder(driveTrain.getLeftEncPos(), 4096, WHEEL_DIAMETER_INCHES * 0.0254);
        right.configureEncoder(driveTrain.getRightEncPos(), 4096, WHEEL_DIAMETER_INCHES * 0.0254);

        left.configurePIDVA(1, 0, 0, 1 / HIGH_SPEED_RANGE, 0);
        right.configurePIDVA(1, 0, 0, 1 / HIGH_SPEED_RANGE, 0);
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        double leftOut = left.calculate(driveTrain.getLeftEncPos());
        double rightOut = right.calculate(driveTrain.getRightEncPos());

        done = leftOut == 0 && rightOut == 0;

        double angleDiff = Pathfinder.boundHalfDegrees(Pathfinder.r2d(left.getHeading()) - driveTrain.getRobotHeading());
        double turn = 0.8 * (-1.0 / 80.0) * angleDiff;

        driveTrain.setSpeedsRaw(leftOut + turn, rightOut - turn);
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        driveTrain.setSpeedsPercent(0, 0);
    }

    /**
     * Called when the command ends because somebody called {@link Command#cancel() cancel()} or
     * another command shared the same requirements as this one, and booted it out.
     *
     * <p>This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     *
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method within this
     * method, as done here.
     */
    @Override
    protected void interrupted() {
        end();
    }

    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     *
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return done;
    }
}
