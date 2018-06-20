package org.team639.robot.commands.leds;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.robot.subsystems.LEDSubsystem;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/20/2018.
 * Part of 2018-offseason-experiments.
 */
public class OneOn extends Command {
    private LEDSubsystem subsystem;

    public OneOn(LEDSubsystem subsystem) {
        this.subsystem = subsystem;
        requires(subsystem);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        subsystem.setLed1(true);
        subsystem.setLed2(false);
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        super.execute();
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
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     * <p>
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     * for this.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return false;
    }
}
