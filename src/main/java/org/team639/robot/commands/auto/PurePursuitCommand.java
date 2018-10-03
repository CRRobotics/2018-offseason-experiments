package org.team639.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.Vector2d;
import org.team639.robot.Robot;
import org.team639.robot.subsystems.DriveTrain;

import java.util.ArrayList;
import java.util.List;

public class PurePursuitCommand extends Command {

    private DriveTrain driveTrain;

    public PurePursuitCommand(Waypoint[] waypoints) {
        driveTrain = Robot.getDriveTrain();
        requires(driveTrain);
    }

    public List<Waypoint> fill(Waypoint[] waypoints, double spacing) {
        List<Waypoint> newPoints = new ArrayList<>();
        newPoints.add(waypoints[0]);
        for (int i = 0; i < waypoints.length - 1; i++) {
            Vector vec = waypoints[i + 1].minus(waypoints[i]);
            int numPointa = (int) Math.floor(vec.magnitude() / spacing); // Change to math.ceil()?
            Vector spacingVec = vec.normalize().scalarMultiply(spacing);
            for (int j = 0; j <= numPointa; j++) {
                newPoints.add(waypoints[i].plus(spacingVec.scalarMultiply(j)));
            }
            newPoints.add(waypoints[i + 1]);
        }
        return newPoints;
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        super.initialize();
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        super.execute();
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
        return false;
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        super.end();
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
        super.interrupted();
    }
}
