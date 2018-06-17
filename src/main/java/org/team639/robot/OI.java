package org.team639.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import org.team639.lib.controls.DoubleLogitechAttack3;
import org.team639.lib.controls.JoystickManager;
import org.team639.lib.controls.LogitechF310;
import org.team639.robot.commands.drive.AutoTurnToAngle;
import org.team639.robot.commands.drive.ShiftLow;
import org.team639.robot.commands.drive.ZeroYaw;

import static org.team639.lib.controls.LogitechF310.ControllerAxis.LeftTrigger;
import static org.team639.robot.Constants.JOYSTICK_DEADZONE;

/**
 * Operator Interface
 * Manages the interaction between the drive team and the robot
 * Part of 2018-offseason-experiments.
 */
public class OI {

    public static final JoystickManager drive = new DoubleLogitechAttack3(0, 1, JOYSTICK_DEADZONE);

    /**
     * Maps all of the buttons.
     * THIS MUST BE RUN AT THE END OF robotInit in Robot.java!!!
     */
    public static void mapButtons() {

//        Button driveAcqRaise = new Button() {
//            @Override
//            public boolean get() {
//                return drive.getControllerAxis(LeftTrigger) >= 0.25;
//            }
//        };
//        drive.mapButton(LogitechF310.Buttons.LB, new ShiftLow(), JoystickManager.MappingType.WhenPressed);
//        drive.mapButton(LogitechF310.Buttons.B, new ZeroYaw(), JoystickManager.MappingType.WhenPressed);
//
//        drive.mapButton(LogitechF310.Buttons.POVLeft, new AutoTurnToAngle(180), JoystickManager.MappingType.WhenPressed);
//        drive.mapButton(LogitechF310.Buttons.POVUp, new AutoTurnToAngle(90), JoystickManager.MappingType.WhenPressed);
//        drive.mapButton(LogitechF310.Buttons.POVRight, new AutoTurnToAngle(0), JoystickManager.MappingType.WhenPressed);
//        drive.mapButton(LogitechF310.Buttons.POVDown, new AutoTurnToAngle(270), JoystickManager.MappingType.WhenPressed);
    }


    /**
     * Maps the specified command to the specified button
     * @param condition A custom condition to map to
     * @param cmd The command to map
     * @param type The type of mapping
     */
    public static void mapCondition(Button condition, Command cmd, JoystickManager.MappingType type) {
        switch (type) {
            case WhenPressed:
                condition.whenPressed(cmd);
                break;
            case WhenReleased:
                condition.whenReleased(cmd);
                break;
            case WhileHeld:
                condition.whileHeld(cmd);
                break;
            case CancelWhenPressed:
                condition.cancelWhenPressed(cmd);
                break;
            case ToggleWhenPressed:
                condition.toggleWhenPressed(cmd);
                break;
        }
    }

    private OI() {
    }
}

