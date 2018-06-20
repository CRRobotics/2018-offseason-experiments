package org.team639.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.team639.robot.commands.leds.Off;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/20/2018.
 * Part of 2018-offseason-experiments.
 */
public abstract class LEDSubsystem extends Subsystem {

    public abstract void setLed1(boolean on);

    public abstract void setLed2(boolean on);

    public abstract boolean getLed1();

    public abstract boolean getLed2();

    /**
     * Initialize the default command for a subsystem By default subsystems have no default command,
     * but if they do, the default command is set with this method. It is called on all Subsystems by
     * CommandBase in the users program after all the Subsystems are created.
     */
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new Off(this));
    }


}
