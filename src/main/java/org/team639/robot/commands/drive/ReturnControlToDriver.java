package org.team639.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.robot.Robot;

/**
 * A command that interrupts all other drive commands, returning control to the human driver.
 */
public class ReturnControlToDriver extends Command {

    public ReturnControlToDriver() {
        super("ReturnControlToDriver");
        requires(Robot.getDriveTrain());
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
