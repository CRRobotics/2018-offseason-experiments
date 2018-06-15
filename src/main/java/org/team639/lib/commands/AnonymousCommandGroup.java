package org.team639.lib.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A CommandGroup that can be instantiated anonymously.
 */
public abstract class AnonymousCommandGroup extends CommandGroup {
    public AnonymousCommandGroup() {
        init();
    }

    /**
     * Place addParallel and addSequential calls that would normally go in the CommandGroup constructor here.
     */
    protected abstract void init();

    /**
     * Returns true if all the {@link Command Commands} in this group have been started and have
     * finished.
     * <p>
     * <p> Teams may override this method, although they should probably reference super.isFinished()
     * if they do. </p>
     *
     * @return whether this {@link CommandGroup} is finished
     */
    @Override
    public boolean isFinished() {
        return super.isFinished();
    }
}
