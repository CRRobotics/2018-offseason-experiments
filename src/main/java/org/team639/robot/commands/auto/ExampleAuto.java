package org.team639.robot.commands.auto;

import org.team639.robot.Robot;

/**
 * Basic test of new auto system.
 */
public class ExampleAuto extends BasicAutoCommand {
    public ExampleAuto() {
        super(Robot.getStartingPosition());
    }

    /**
     * Implement this method to create auto routine.
     */
    @Override
    protected void sequence() {
        goToPosition(50, 50);
        goToPosition(200, 50);
        goToPosition(225, 100);
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {

    }
}
