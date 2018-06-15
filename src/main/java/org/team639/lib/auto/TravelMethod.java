package org.team639.lib.auto;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Created by Jack Greenberg <theProgrammerJack@gmail.com> on 6/15/2018.
 * Part of 2018-offseason-experiments.
 */
public interface TravelMethod {
    Command generate(Position destination, Position current);
}
