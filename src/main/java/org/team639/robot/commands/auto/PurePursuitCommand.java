package org.team639.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Trajectory;
import org.team639.lib.math.AngleMath;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import static org.team639.robot.Constants.DriveTrain.M_PER_S_TO_ENC_UNITS;

public class PurePursuitCommand extends Command {

    public static final double TOLERANCE = 0.05;
    public static final double ANGLE_P = 0.01;

    private DriveTrain driveTrain;

    private Trajectory trajectory;

    public PurePursuitCommand(Trajectory trajectory) {
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
        this.trajectory = trajectory;
    }

    private int segment;
    private boolean done;

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        segment = 0;
        done = false;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        double x = Robot.getTrackedX();
        double y = Robot.getTrackedY();

        Trajectory.Segment seg = trajectory.get(segment);
        if (AngleMath.distance(x, y, seg.x, seg.y) <= TOLERANCE) {
            segment++;
            done = segment >= trajectory.length();
        } else {
            double angle = Math.toDegrees(Math.atan2(seg.y - y, seg.x - x));
            double velocity = seg.velocity * M_PER_S_TO_ENC_UNITS;
            double currentAngle = driveTrain.getRobotAngle();

            double diff = ANGLE_P * AngleMath.shortestAngle(currentAngle, angle);
//            System.out.println("left: " + (velocity - diff) + ", right: " + (velocity + diff));
            driveTrain.setSpeedsMPS(velocity - diff, velocity + diff);
        }
    }

    /**
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     * <p>
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
     * <p>
     * <p>This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     * <p>
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method within this
     * method, as done here.
     */
    @Override
    protected void interrupted() {
        end();
    }
}
