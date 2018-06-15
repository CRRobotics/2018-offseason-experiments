package org.team639.lib.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.commands.AnonymousCommandGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class representing an action taken during an auto routine. Can consist of one driving destination and multiple other commands.
 *
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/14/2018.
 * Part of 2018-offseason-experiments.
 */
public class Action {
    private List<Command> commands;
    private Optional<Position> destination;
    private Optional<TravelMethod> travelMethod;

    private AnonymousCommandGroup group;
    private boolean running = false;

    /**
     * Create an empty Action.
     */
    public Action() {
        commands = new ArrayList<>();
        destination = Optional.empty();
        travelMethod = Optional.empty();
    }

    /**
     * Create an Action containing only the given Command
     * @param cmd The command to include.
     */
    public Action(Command cmd) {
        this();
        commands.add(cmd);
    }

    /**
     * Create an Action with the given position as the target destination.
     * @param pos The target destination.
     */
    public Action(Position pos) {
        this(pos, null);
    }

    /**
     * Create an Action with the given position as the target destination and the given method as the way to get there.
     * @param pos The target destination.
     * @param method The way to get to the target.
     */
    public Action(Position pos, TravelMethod method) {
        destination = Optional.of(pos);
        travelMethod = Optional.ofNullable(method);
    }

    /**
     * Adds the given Command to the Action
     * @param cmd The Command to add.
     * @return The Action, to allow for chaining.
     */
    public Action with(Command cmd) {
        commands.add(cmd);
        return this;
    }

    /**
     * Returns the target destination of the Action.
     * @return The target destination of the Action.
     */
    Optional<Position> getDestination() {
        return destination;
    }

    Optional<TravelMethod> getTravelMethod() {
        return travelMethod;
    }

    /**
     * Returns whether or not the Action is currently running.
     * @return Whether or not the Action is currently running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Begins running the Action.
     */
    public void begin() {
        group = new AnonymousCommandGroup() {
            @Override
            protected void init() {
                for (Command cmd : commands) {
                    addParallel(cmd);
                }
            }
        };
        group.start();
        running = true;
    }

    /**
     * Returns whether or not all parts of the action have finished executing.
     * @return Whether or not all parts of the action have finished executing.
     */
    public boolean isFinished() {
        running = !group.isFinished();
        return group.isFinished();
    }

    /**
     * Interrupts the Action, stopping its execution.
     */
    public void interrupt() {
        group.cancel();
        running = false;
    }
}
