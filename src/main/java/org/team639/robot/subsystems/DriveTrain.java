package org.team639.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.team639.robot.Constants;
import org.team639.robot.RobotMap;
import org.team639.robot.commands.drive.JoystickDrive;

import static org.team639.robot.Constants.DriveTrain.*;

/**
 * Contains all methods relating to the drivetrain
 */
public class DriveTrain extends Subsystem {
    private TalonSRX leftDrive;
    private IMotorController leftFollower1;
    private IMotorController leftFollower2;

    private TalonSRX rightDrive;
    private IMotorController rightFollower1;
    private IMotorController rightFollower2;

    private Solenoid shifter;

    private double kP;
    private double kI;
    private double kD;
    private double kF;

    private double rampRate = 0;

    private AHRS ahrs;

    private DriveGear currentGear;
    private ControlMode currentControlMode;

    /**
     * The gears of the robot.
     */
    public enum DriveGear {
        High,
        Low,
    }

    /**
     * Constructs a new DriveTrain and configures all of the motor controllers and sensors.
     */
    public DriveTrain() {
        leftDrive = RobotMap.getLeftDrive();
        leftFollower1 = RobotMap.getLeftFollower1();
        leftFollower2 = RobotMap.getLeftFollower2();

        rightDrive = RobotMap.getRightDrive();
        rightFollower1 = RobotMap.getRightFollower1();
        rightFollower2 = RobotMap.getRightFollower2();

//        rightDrive.setInverted(true);
        leftDrive.setInverted(true);
        leftFollower1.setInverted(true);
        leftFollower2.setInverted(true);

        leftDrive.configAllowableClosedloopError(0,50,0);
        rightDrive.configAllowableClosedloopError(0,50,0);

        leftDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        rightDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);

//        leftDrive.setSensorPhase(true);

        leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 20, 0);
        rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 20, 0);

        setNeutralMode(NeutralMode.Brake);

        leftDrive.configClosedloopRamp(0, 0);
        rightDrive.configClosedloopRamp(0, 0);


        leftFollower1.follow(leftDrive);
        leftFollower2.follow(leftDrive);

        rightFollower1.follow(rightDrive);
        rightFollower2.follow(rightDrive);

        setCurrentControlMode(ControlMode.Velocity);

        shifter = RobotMap.getDriveShifter();
        setCurrentGear(DriveGear.High);

        setRampRate(rampRate); // TODO: Maybe change ramp rate with gear

        ahrs = RobotMap.getAhrs();
        ahrs.zeroYaw();
    }

    /**
     * Returns the current Talon control mode
     * @return The current Talon control mode
     */
    public ControlMode getCurrentControlMode() {
        return currentControlMode;
    }

    /**
     * Sets the Talon control mode
     * @param mode The control mode to set the talons to
     */
    public void setCurrentControlMode(ControlMode mode) {
        currentControlMode = mode;
//        leftDrive.changeControlMode(currentControlMode);
//        rightDrive.changeControlMode(currentControlMode);
    }

    /**
     * Sets the PID constants.
     * @param p The P constant.
     * @param i The I constant.
     * @param d The D constant.
     * @param f The F constant.
     */
    public void setPID(double p, double i, double d, double f) {
        kP = p;
        kI = i;
        kD = d;
        kF = f;
        rightDrive.config_kP(0, kP, 0);
        rightDrive.config_kI(0, kI, 0);
        rightDrive.config_kD(0, kD, 0);
        rightDrive.config_kF(0, kF, 0);
        leftDrive.config_kP(0, kP, 0);
        leftDrive.config_kI(0, kI, 0);
        leftDrive.config_kD(0, kD, 0);
        leftDrive.config_kF(0, kF, 0);

    }

    /**
     * Sets the default command to run while no others are running
     */
    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickDrive());
    }

    /**
     * Takes two speed values from -1 to 1 and uses them to set the motors
     * @param lSpeed The value for the left side
     * @param rSpeed The value for the right side
     */
    public void setSpeedsPercent(double lSpeed, double rSpeed) {
        double range = currentGear == DriveGear.High ? HIGH_SPEED_RANGE : LOW_SPEED_RANGE;
        double l_vel = getLeftEncVelocity();
        double r_vel = getRightEncVelocity();

        double avg_vel = Math.abs((l_vel + r_vel) / 2);
        double avg_cmd = Math.abs((lSpeed + rSpeed) / 2) * range;

        // Limits speeds to the range [-1, 1]
        if (Math.abs(lSpeed) > 1) lSpeed = lSpeed < 0 ? -1 : 1;
        if (Math.abs(rSpeed) > 1) rSpeed = rSpeed < 0 ? -1 : 1;
        switch (currentControlMode) {
            case PercentOutput:
                setSpeedsRaw(lSpeed, rSpeed);
                break;
            case Velocity:
                SmartDashboard.putNumber("right setpoint", rSpeed * range);
                SmartDashboard.putNumber("left setpoint", lSpeed * range);
                double ls = lSpeed * range;
                double rs = rSpeed * range;
                setSpeedsRaw(ls, rs);
                break;
        }
    }

    /**
     * Takes two raw speed values and uses them to set the motors.
     * @param lSpeed The value for the left side
     * @param rSpeed The value for the right side
     */
    public void setSpeedsRaw(double lSpeed, double rSpeed) {
        rightDrive.set(currentControlMode, rSpeed);
        leftDrive.set(currentControlMode, lSpeed);
    }

    /**
     * Takes velocity values in meters per second and sets speeds accordingly.
     * @param left
     * @param right
     */
    public void setSpeedsMPS(double left, double right) {
        setCurrentControlMode(ControlMode.Velocity);
        System.out.println("uh oh: " + M_PER_S_TO_ENC_UNITS + "; left: " + left + "," + (left * M_PER_S_TO_ENC_UNITS) + "; right: " + right + ", " + (right * M_PER_S_TO_ENC_UNITS));
        setSpeedsRaw(left * M_PER_S_TO_ENC_UNITS, right * M_PER_S_TO_ENC_UNITS);
    }

    /**
     * Returns the position of the left encoder
     * @return The position of the left encoder
     */
    public int getLeftEncPos() {
        return leftDrive.getSelectedSensorPosition(0);
    }

    /**
     * Reutrns the position of the right encoder
     * @return The position of the right encoder
     */
    public int getRightEncPos() {
        return rightDrive.getSelectedSensorPosition(0);
    }

    /**
     * Returns the velocity of the left encoder
     * @return The velocity of the left encoder
     */
    public int getLeftEncVelocity() {
        return leftDrive.getSelectedSensorVelocity(0);
    }

    /**
     * Returns the velocity of the right encoder
     * @return The velocity of the right encoder
     */
    public int getRightEncVelocity() {
        return rightDrive.getSelectedSensorVelocity(0);
    }

    /**
     * Gets the current yaw of the robot from 0-360 degrees, with 90 being directly downfield. This assumes that the robot starts facing downfield.
     * @return The current yaw of the robot
     */
    public double getRobotAngle() {
        double angle = ahrs.getYaw();
        angle *= -1;
        angle += 90;
        if (angle < 0) angle = 360 + angle;
        return angle;
    }

    public double getRobotHeading() {
        return ahrs.getYaw();
    }

    /**
     * Checks if the NavX is connected.
     * @return If the NavX is connected.
     */
    public boolean isNavXPresent() {
        return ahrs.isConnected();
    }

    /**
     * Zeroes the robot yaw.
     */
    public void zeroRobotYaw() {
        ahrs.zeroYaw();
    }

    /**
     * Returns the P constant of the drivetrain.
     * @return the P constant of the drivetrain.
     */
    public double getkP() {
        return kP;
    }

    /**
     * Returns the I constant of the drivetrain.
     * @return the I constant of the drivetrain.
     */
    public double getkI() {
        return kI;
    }

    /**
     * Returns the D constant of the drivetrain.
     * @return the D constant of the drivetrain.
     */
    public double getkD() {
        return kD;
    }

    /**
     * Returns the F constant of the drivetrain.
     * @return the F constant of the drivetrain.
     */
    public double getkF() {
        return kF;
    }

    /**
     * Returns the ramp rate of the drivetrain.
     * @return the ramp rate of the drivetrain.
     */
    public double getRampRate() {
        return rampRate;
    }

    /**
     * Sets the ramp rate of the drivetrain
     * @param rampRate The new ramp rate.
     */
    public void setRampRate(double rampRate) {
        this.rampRate = rampRate;
        leftDrive.configClosedloopRamp(rampRate, 0);
        rightDrive.configClosedloopRamp(rampRate, 0);
    }

    /**
     * Changes the gear, including adjusting the PID values.
     * @param gear The gear to change to.
     */
    public void setCurrentGear(DriveGear gear) {
        //if(gear == currentGear) return; // optimization that might break debugging stuff
        this.currentGear = gear;
        switch (gear) {
            case Low:
                shifter.set(true);
                setPID(HIGH_DRIVE_P, HIGH_DRIVE_I, HIGH_DRIVE_D, HIGH_DRIVE_F);
                break;
            case High:
                shifter.set(false);
                setPID(LOW_DRIVE_P, LOW_DRIVE_I, LOW_DRIVE_D, LOW_DRIVE_F);
                break;
        }
    }

    /**
     * Returns the current drive gear.
     * @return The current drive gear.
     */
    public DriveGear getCurrentGear() {
        return currentGear;
    }

    /**
     * Returns whether or not the encoders are both connected.
     * @return Whether or not the encoders are both connected.
     */
    public boolean encodersPresent() {
        return !Constants.REAL || !(rightDrive.getSensorCollection().getPulseWidthRiseToRiseUs() == 0 || leftDrive.getSensorCollection().getPulseWidthRiseToRiseUs() == 0);
    }

    /**
     * Changes the neutral mode of the motor controllers.
     * @param mode The neutral mode.
     */
    public void setNeutralMode(NeutralMode mode) {
        leftDrive.setNeutralMode(mode);
        leftFollower1.setNeutralMode(mode);
        leftFollower2.setNeutralMode(mode);
        rightDrive.setNeutralMode(mode);
        rightFollower1.setNeutralMode(mode);
        rightFollower2.setNeutralMode(mode);
    }
}
