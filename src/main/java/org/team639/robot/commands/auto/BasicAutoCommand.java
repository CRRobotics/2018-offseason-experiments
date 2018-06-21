package org.team639.robot.commands.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.auto.AutoCommand;
import org.team639.lib.auto.Position;
import org.team639.lib.commands.AnonymousCommandGroup;
import org.team639.lib.math.AngleMath;
import org.team639.robot.Robot;
import org.team639.robot.commands.drive.AutoDriveForward;
import org.team639.robot.commands.drive.AutoTurnToAngle;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/15/2018.
 * Part of 2018-offseason-experiments.
 */
public abstract class BasicAutoCommand extends AutoCommand {

    private Position startPos;

    public BasicAutoCommand(StartingPosition stPos) {
        startPos = stPos.position.clone();
    }

    /**
     * Called every time the robot needs to travel to generate a command that can handle that.
     *
     * @param destination The intended destination of the robot.
     * @param current     The current position of the robot.
     * @return A command that will move the robot to the destination.
     */
    @Override
    protected Command travelMethod(Position destination, Position current) {
        double angle = Math.toDegrees(Math.atan2(destination.y - current.y, destination.x - current.x));
        double distance = AngleMath.distance(destination.x, destination.y, current.x, current.y);
        return new AnonymousCommandGroup() {
            @Override
            protected void init() {
                addSequential(new AutoTurnToAngle(angle));
                addSequential(new AutoDriveForward(distance, angle));
            }
        };
    }

    /**
     * Returns the current position of the robot.
     *
     * @return The current position of the robot.
     */
    @Override
    protected Position currentPosition() {
        if (getLastDestination().isPresent()) {
            Position pos = getLastDestination().get();
            return new Position(pos.x, pos.y, Robot.getDriveTrain().getRobotYaw());
        } else {
            return new Position(startPos.x, startPos.y, Robot.getDriveTrain().getRobotYaw());
        }
    }
}
