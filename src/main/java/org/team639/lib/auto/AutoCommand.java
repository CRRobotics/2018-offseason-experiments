package org.team639.lib.auto;

import edu.wpi.first.wpilibj.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class that can be extended to easily create auto routines.
 *
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/14/2018.
 * Part of 2018-offseason-experiments.
 */
public abstract class AutoCommand extends Command {
    private List<Action> actions;

    private Optional<Position> lastDestination;

    /**
     * Creates a new AutoCommand
     */
    public AutoCommand() {
    }

    /**
     * Implement this method to create auto routine.
     * Call {@link #goToPosition(Position)} and {@link #doCommand(Command)} to add actions.
     * Chain {@link Action#with(Command)} calls afterwards to add more Commands in parallel.
     */
    protected abstract void sequence();

    /**
     * Called every time the robot needs to travel to generate a command that can handle that.
     * Can be overridden on a per-Action basis by passing a {@link TravelMethod} to {@link Action#Action(Position, TravelMethod)}
     * @param destination The intended destination of the robot.
     * @param current The current position of the robot.
     * @return A command that will move the robot to the destination.
     */
    protected abstract Command travelMethod(Position destination, Position current);

    /**
     * Returns the current position of the robot.
     * @return The current position of the robot.
     */
    protected abstract Position currentPosition();

    /**
     * Returns the last destination specified by the auto routine.
     * @return The last destination specified by the auto routine.
     */
    protected Optional<Position> getLastDestination() {
        return lastDestination;
    }

    /**
     * Tells the robot to travel to a position.
     * @param p The position to travel to.
     * @return The created {@link Action}, for chaining {@link Action#with(Command)}
     */
    protected Action goToPosition(Position p) {
        Action tpa = new Action(p);
        actions.add(tpa);
        return tpa;
    }

    /**
     * Tells the robot to travel to a position.
     * @param x The x position to travel to.
     * @param y The y position to travel to.
     * @return The created {@link Action}, for chaining {@link Action#with(Command)}
     */
    protected Action goToPosition(double x, double y) {
        Action tpa = new Action(new Position(x, y));
        actions.add(tpa);
        return tpa;
    }

    /**
     * Tells the robot to travel to a position.
     * @param x The x position to travel to.
     * @param y The y position to travel to.
     * @param angle The angle that the robot should be facing.
     * @return The created {@link Action}, for chaining {@link Action#with(Command)}
     */
    protected Action goToPosition(double x, double y, double angle) {
        Action tpa = new Action(new Position(x, y, angle % 360));
        actions.add(tpa);
        return tpa;
    }

    /**
     * Perform a command while stationary.
     * @param cmd The Command to execute.
     * @return The created {@link Action}, for chaining {@link Action#with(Command)}
     */
    protected Action doCommand(Command cmd) {
        Action a = new Action(cmd);
        actions.add(a);
        return a;
    }

    /**
     * Launches a specified command in parallel such that it is not influenced and can not influence the runnning time
     * of other parts of the auto routine.
     * Can be waited for at a later point using {@link #waitFor(Action)}.
     * Should not be used with any command that manipulates the drive train or other systems necessary for driving.
     * @param cmd The command to launch in parallel.
     * @return The action, for chaining with {@link Action#with(Command)} if desired or using with {@link #waitFor(Action)}
     */
    protected Action launch(Command cmd) {
        Action a = Action.parallel(cmd);
        actions.add(a);
        return a;
    }

    /**
     * Waits for an Action launched in parallel to finish.
     * If a non parallel Action is passed in, nothing will happen.
     * @param parallelAction The Action to wait for.
     */
    protected void waitFor(Action parallelAction) {
        if (parallelAction.isParallel()) {
            actions.add(new Action() {
                /**
                 * Begins running the Action.
                 */
                @Override
                public void begin() {
                }

                /**
                 * Returns whether or not all parts of the action have finished executing.
                 *
                 * @return Whether or not all parts of the action have finished executing.
                 */
                @Override
                public boolean isFinished() {
                    return parallelAction.isFinished();
                }
            });
        }
    }

    /**
     * Interrupts an Action launched in parallel if it isn't finished.
     * If a non parallel Action is passed in, nothing will happen.
     * @param parallelAction The Action to interrupt.
     */
    protected void interruptParallel(Action parallelAction) {
        if (parallelAction.isParallel() && !parallelAction.isFinished()) {
            actions.add(new Action() {
                /**
                 * Begins running the Action.
                 */
                @Override
                public void begin() {
                    parallelAction.interrupt();
                }

                /**
                 * Returns whether or not all parts of the action have finished executing.
                 *
                 * @return Whether or not all parts of the action have finished executing.
                 */
                @Override
                public boolean isFinished() {
                    return true;
                }
            });
        }
    }


    // Command framework compatibility stuff below

    private int step;
    private boolean running;
    private List<Action> parallelActions;

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        lastDestination = Optional.empty();
        actions = new ArrayList<>();
        parallelActions = new ArrayList<>();
        sequence();

        step = 0;
        running = true;
        System.out.println("AutoCommand initialized");
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        System.out.println("AutoCommand execute");
        if (step >= actions.size()) {
            if (parallelActions.stream().allMatch(Action::isFinished)) running = false;
            return;
        }
        Action currentAction = actions.get(step);
        if (currentAction.isStarted()) {
            if (currentAction.isFinished() || (currentAction.isParallel() && step < actions.size() - 1)) {
                step++;
                if (step >= actions.size()) running = false;
            }
        } else {
            // Add driving command if there is a destination specified
            if (currentAction.getDestination().isPresent()) {
                Command traveler;
                // Use alternate travel method instead of default, if specified.
                if (currentAction.getTravelMethod().isPresent()) {
                    traveler = currentAction.getTravelMethod().get().generate(currentAction.getDestination().get(), currentPosition());
                    System.out.println("Other travel method specified");
                } else {
                    System.out.println("Using default travel method");
                    traveler = travelMethod(currentAction.getDestination().get(), currentPosition());
                }
                currentAction.with(traveler);
                lastDestination = Optional.of(currentAction.getDestination().get().clone());
            }

            if (currentAction.isParallel()) parallelActions.add(currentAction);

            currentAction.begin();
        }
    }

    /**
     * Called when the command ended peacefully. This is where you may want to wrap up loose ends,
     * like shutting off a motor that was being used in the command.
     */
    @Override
    protected void end() {
        running = false;
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
        actions.get(step).interrupt();
        running = false;
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
        if (!running) System.out.println("AutoCommand finished");
        return !running;
    }
}
