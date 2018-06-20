package org.team639.robot.commands.leds;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.robot.subsystems.LEDSubsystem;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/20/2018.
 * Part of 2018-offseason-experiments.
 */
public class Off extends Command {
    private LEDSubsystem subsystem;
    public Off(LEDSubsystem subsystem) {
        this.subsystem = subsystem;
        requires(subsystem);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        subsystem.setLed1(false);
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
     * Returns whether this command is finished. If it is, then the command will be removed and {@link
     * Command#end() end()} will be called.
     * <p>
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()}
     * method for time-sensitive commands.
     * <p>
     * <p>Returning false will result in the command never ending automatically. It may still be
     * cancelled manually or interrupted by another command. Returning true will result in the
     * command executing once and finishing immediately. We recommend using {@link InstantCommand}
     * for this.
     *
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    @Override
    protected boolean isFinished() {
        return true;
    }
}
