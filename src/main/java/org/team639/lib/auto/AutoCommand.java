package org.team639.lib.auto;

import edu.wpi.first.wpilibj.command.Command;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * A class that can be extended to easily create auto routines.
 *
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/14/2018.
 * Part of 2018-offseason-experiments.
 */
public abstract class AutoCommand extends Command {
    private List<Action> actions;
    private Queue<Position> positions;

    private Optional<Position> lastDestination;

    /**
     * Creates a new AutoCommand
     */
    public AutoCommand() {
        lastDestination = Optional.empty();
        actions = new LinkedList<>();
        sequence();
    }

    /**
     * Implement this method to create auto routine.
     * Call {@link #goToPosition(Position)} and {@link #doCommand(Command)} to add actions.
     */
    protected abstract void sequence();

    /**
     * Called every time the robot needs to travel to generate a command that can handle that.
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
     * @return The created action, for chaining {@link Action#with(Command)}
     */
    protected Action goToPosition(Position p) {
        Action tpa = new Action(p);
        actions.add(tpa);
        return tpa;
    }

    /**
     * Perform a command while stationary.
     * @param cmd The Command to execute.
     * @return The created action, for chaining {@link Action#with(Command)}
     */
    protected Action doCommand(Command cmd) {
        Action a = new Action(cmd);
        actions.add(a);
        return a;
    }


    // Command stuff below

    private int step;
    private boolean running;

    /**
     * The initialize method is called the first time this Command is run after being started.
     */
    @Override
    protected void initialize() {
        step = 0;
        running = true;
    }

    /**
     * The execute method is called repeatedly until this Command either finishes or is canceled.
     */
    @Override
    protected void execute() {
        if (step >= actions.size()) {
            running = false;
            return;
        }
        Action currentAction = actions.get(step);
        if (currentAction.isRunning()) {
            if (currentAction.isFinished()) {
                step++;
                if (step >= actions.size()) running = false;
            }
        } else {
            // Add driving command if there is a destination specified
            if (currentAction.getDestination().isPresent()) {
                currentAction.with(travelMethod(currentAction.getDestination().get(), currentPosition()));
                lastDestination = Optional.of(currentAction.getDestination().get().clone());
            }

            currentAction.begin();
        }
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
        return !running;
    }
}
