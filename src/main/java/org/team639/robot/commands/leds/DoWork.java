package org.team639.robot.commands.leds;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.robot.subsystems.LEDSubsystem;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/20/2018.
 * Part of 2018-offseason-experiments.
 */
public class DoWork extends Command {
    private LEDSubsystem subsystem;
    private double last;

    public DoWork(LEDSubsystem subsystem, int duration) {
        this.subsystem = subsystem;
        setTimeout(duration);
        requires(subsystem);
    }

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        last = 0;
        subsystem.setLed1(true);
        subsystem.setLed2(false);
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        System.out.println("Doing work");
        double temp = timeSinceInitialized();
        if (temp - last >= 0.5) {
            last = temp;
            subsystem.setLed1(!subsystem.getLed1());
            subsystem.setLed2(!subsystem.getLed2());
        }
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        subsystem.setLed1(false);
        subsystem.setLed2(false);
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
        return isTimedOut();
    }
}
