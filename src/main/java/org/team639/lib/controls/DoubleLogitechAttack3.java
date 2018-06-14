package org.team639.lib.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class DoubleLogitechAttack3 extends JoystickManager {

    private Joystick leftStick;
    private Joystick rightStick;

    public DoubleLogitechAttack3() {
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
    }

    public DoubleLogitechAttack3(int port1, int port2) {
        leftStick = new Joystick(port1);
        rightStick = new Joystick(port2);
    }

    /**
     * Returns the Y axis value of the left drive Joystick with forward being 1 and backward being -1
     *
     * @return The Y axis value of the left drive Joystick
     */
    @Override
    public double getLeftStickY() {
        return -1 * leftStick.getRawAxis(1);
    }

    /**
     * Returns the Y axis value of the right drive Joystick with forward being 1 and backward being -1
     *
     * @return The Y axis value of the right drive Joystick
     */
    @Override
    public double getRightStickY() {
        return -1 * rightStick.getRawAxis(1);
    }

    /**
     * Returns the X axis value of the left driveJoystick with to the right being 1 and to the left being -1
     *
     * @return The X axis value of the left drive Joystick
     */
    @Override
    public double getLeftStickX() {
        return leftStick.getRawAxis(0);
    }

    /**
     * Returns the X axis value of the left drive Joystick with to the right being 1 and to the left being -1
     *
     * @return The X axis value of the left drive Joystick
     */
    @Override
    public double getRightStickX() {
        return rightStick.getRawAxis(0);
    }

    /**
     * Maps the specified command to the specified button
     *
     * @param btn  The location of the button
     * @param cmd  The command to map
     * @param type The type of mapping
     */
    @Override
    public void mapButton(ButtonType btn, Command cmd, MappingType type) {

    }

    /**
     * Returns whether a button is pressed
     *
     * @param btn The button to retrieve
     * @return Whether or not the button is pressed.
     */
    @Override
    public boolean getButtonPressed(ButtonType btn) {
        return false;
    }

    /**
     * Returns the value from the specified controller axis from -1 to 1
     *
     * @param axis The controller axis to return
     * @return the value from the specified controller axis
     */
    @Override
    public double getControllerAxis(ControllerAxisType axis) {
        return 0;
    }
}
