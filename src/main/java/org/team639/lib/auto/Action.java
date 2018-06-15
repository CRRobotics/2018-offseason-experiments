package org.team639.lib.auto;

import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.commands.AnonymousCommandGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/14/2018.
 * Part of 2018-offseason-experiments.
 */
public class Action {
    private List<Command> commands;
    private Optional<Position> destination;

    private AnonymousCommandGroup group;
    private boolean running = false;

    public Action() {
        commands = new ArrayList<>();
        destination = Optional.empty();
    }

    public Action(Command cmd) {
        this();
        commands.add(cmd);
    }

    public Action(Position p) {
        destination = Optional.of(p);
    }

    public Action with(Command cmd) {
        commands.add(cmd);
        return this;
    }

    Optional<Position> getDestination() {
        return destination;
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {
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

    public boolean isFinished() {
        running = !group.isFinished();
        return group.isFinished();
    }

    public void interrupt() {
        group.cancel();
        running = false;
    }
}
