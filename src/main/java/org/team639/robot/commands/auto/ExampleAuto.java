package org.team639.robot.commands.auto;

import org.team639.lib.auto.Action;
import org.team639.robot.Robot;
import org.team639.robot.commands.leds.DoWork;

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
//        goToPosition(32, 24).with(new DoWork(Robot.getRedLEDs(), 6));
//        doCommand(new DoWork(Robot.getBlueLEDs(), 3));
//        Action work1 = launch(new DoWork(Robot.getGreenLEDs(), 10));
//        goToPosition(0, 0);
//        waitFor(work1);
//        Action work2 = launch(new DoWork(Robot.getRedLEDs(), 10));
//        goToPosition(-50, 50);
//        interruptParallel(work2);

        doCommand(new DoWork(Robot.getBlueLEDs(), 20)).timeout(5);
    }
}
