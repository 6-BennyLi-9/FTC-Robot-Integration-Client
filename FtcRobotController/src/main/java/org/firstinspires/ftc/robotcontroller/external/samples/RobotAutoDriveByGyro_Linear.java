/* Copyright (c) 2022 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/*
 *  This OpMode illustrates the concept of driving an autonomous path based on Gyro (IMU) heading and encoder counts.
 *  The code is structured as a LinearOpMode
 *
 *  The path to be followed by the robot is built from a series of drive, turn or pause steps.
 *  Each step on the path is defined by a single function call, and these can be strung together in any order.
 *
 *  The code REQUIRES that you have encoders on the drive motors, otherwise you should use: RobotAutoDriveByTime;
 *
 *  This code uses the Universal IMU interface so it will work with either the BNO055, or BHI260 IMU.
 *  To run as written, the Control/Expansion hub should be mounted horizontally on a flat part of the robot chassis.
 *  The REV Logo should be facing UP, and the USB port should be facing forward.
 *  If this is not the configuration of your REV Control Hub, then the code should be modified to reflect the correct orientation.
 *
 *  This sample requires that the drive Motors have been configured with names : left_drive and right_drive.
 *  It also requires that a positive power command moves both motors forward, and causes the encoders to count UP.
 *  So please verify that both of your motors move the robot forward on the first move.  If not, make the required correction.
 *  See the beginning of runOpMode() to set the FORWARD/REVERSE option for each motor.
 *
 *  This code uses RUN_TO_POSITION mode for driving straight, and RUN_USING_ENCODER mode for turning and holding.
 *  Note: This code implements the requirement of calling setTargetPosition() at least once before switching to RUN_TO_POSITION mode.
 *
 *  Notes:
 *
 *  All angles are referenced to the coordinate-frame that is set whenever resetHeading() is called.
 *  In this sample, the heading is reset when the Start button is touched on the Driver Station.
 *  Note: It would be possible to reset the heading after each move, but this would accumulate steering errors.
 *
 *  The angle of movement/rotation is assumed to be a standardized rotation around the robot Z axis,
 *  which means that a Positive rotation is Counter Clockwise, looking down on the field.
 *  This is consistent with the FTC field coordinate conventions set out in the document:
 *  https://ftc-docs.firstinspires.org/field-coordinate-system
 *
 *  Control Approach.
 *
 *  To reach, or maintain a required heading, this code implements a basic Proportional Controller where:
 *
 *      Steering power = Heading Error * Proportional Gain.
 *
 *      "Heading Error" is calculated by taking the difference between the desired heading and the actual heading,
 *      and then "normalizing" it by converting it to a value in the +/- 180 degree range.
 *
 *      "Proportional Gain" is a constant that YOU choose to set the "strength" of the steering response.
 *
 *  Use Android Studio to Copy this Class, and Paste it into your "TeamCode" folder with a new name.
 *  Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Robot: Auto Drive By Gyro", group="Robot")
@Disabled
public class RobotAutoDriveByGyro_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor         leftDrive;
    private DcMotor         rightDrive;
    private IMU             imu;      // Control/Expansion Hub IMU

    private double          headingError;

    // These variable are declared here (as class members) so they can be updated in various methods,
    // but still be displayed by sendTelemetry()
    private double  targetHeading;
    private double  driveSpeed;
    private double  turnSpeed;
    private double  leftSpeed;
    private double  rightSpeed;
    private int     leftTarget;
    private int     rightTarget;

    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 537.7 ;   // eg: GoBILDA 312 RPM Yellow Jacket
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // They can/should be tweaked to suit the specific robot drive train.
    static final double     DRIVE_SPEED             = 0.4;     // Max driving speed for better distance accuracy.
    static final double     TURN_SPEED              = 0.2;     // Max turn speed to limit turn rate.
    static final double     HEADING_THRESHOLD       = 1.0 ;    // How close must the heading get to the target before moving to next step.
                                                               // Requiring more accuracy (a smaller number) will often make the turn take longer to get into the final position.
    // Define the Proportional control coefficient (or GAIN) for "heading control".
    // We define one value when Turning (larger errors), and the other is used when Driving straight (smaller errors).
    // Increase these numbers if the heading does not correct strongly enough (eg: a heavy robot or using tracks)
    // Decrease these numbers if the heading does not settle on the correct value (eg: very agile robot with omni wheels)
    static final double     P_TURN_GAIN            = 0.02;     // Larger is more responsive, but also less stable.
    static final double     P_DRIVE_GAIN           = 0.03;     // Larger is more responsive, but also less stable.


    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
	    this.leftDrive = this.hardwareMap.get(DcMotor.class, "left_drive");
	    this.rightDrive = this.hardwareMap.get(DcMotor.class, "right_drive");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
	    this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	    this.rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        /* The next two lines define Hub orientation.
         * The Default Orientation (shown) is when a hub is mounted horizontally with the printed logo pointing UP and the USB port pointing FORWARD.
         *
         * To Do:  EDIT these two lines to match YOUR mounting configuration.
         */
        final RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        final RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        final RevHubOrientationOnRobot                    orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        // Now initialize the IMU with this mounting orientation
        // This sample expects the IMU to be in a REV Hub and named "imu".
	    this.imu = this.hardwareMap.get(IMU.class, "imu");
	    this.imu.initialize(new IMU.Parameters(orientationOnRobot));

        // Ensure the robot is stationary.  Reset the encoders and set the motors to BRAKE mode
	    this.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	    this.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
	    this.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	    this.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (Display Gyro value while waiting)
        while (this.opModeInInit()) {
	        this.telemetry.addData(">", "Robot Heading = %4.0f", this.getHeading());
	        this.telemetry.update();
        }

        // Set the encoders for closed loop speed control, and reset the heading.
	    this.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	    this.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	    this.imu.resetYaw();

        // Step through each leg of the path,
        // Notes:   Reverse movement is obtained by setting a negative distance (not speed)
        //          holdHeading() is used after turns to let the heading stabilize
        //          Add a sleep(2000) after any step to keep the telemetry data visible for review

	    this.driveStraight(DRIVE_SPEED, 24.0, 0.0);    // Drive Forward 24"
	    this.turnToHeading(TURN_SPEED, -45.0);               // Turn  CW to -45 Degrees
	    this.holdHeading(TURN_SPEED, -45.0, 0.5);   // Hold -45 Deg heading for a 1/2 second

	    this.driveStraight(DRIVE_SPEED, 17.0, -45.0);  // Drive Forward 17" at -45 degrees (12"x and 12"y)
	    this.turnToHeading(TURN_SPEED,  45.0);               // Turn  CCW  to  45 Degrees
	    this.holdHeading(TURN_SPEED,  45.0, 0.5);    // Hold  45 Deg heading for a 1/2 second

	    this.driveStraight(DRIVE_SPEED, 17.0, 45.0);  // Drive Forward 17" at 45 degrees (-12"x and 12"y)
	    this.turnToHeading(TURN_SPEED,   0.0);               // Turn  CW  to 0 Degrees
	    this.holdHeading(TURN_SPEED,   0.0, 1.0);    // Hold  0 Deg heading for 1 second

	    this.driveStraight(DRIVE_SPEED,-48.0, 0.0);    // Drive in Reverse 48" (should return to approx. staring position)

	    this.telemetry.addData("Path", "Complete");
	    this.telemetry.update();
	    this.sleep(1000);  // Pause to display last telemetry message.
    }

    /*
     * ====================================================================================================
     * Driving "Helper" functions are below this line.
     * These provide the high and low level methods that handle driving straight and turning.
     * ====================================================================================================
     */

    // **********  HIGH Level driving functions.  ********************

    /**
    *  Drive in a straight line, on a fixed compass heading (angle), based on encoder counts.
    *  Move will stop if either of these conditions occur:
    *  1) Move gets to the desired position
    *  2) Driver stops the OpMode running.
    *
    * @param maxDriveSpeed MAX Speed for forward/rev motion (range 0 to +1.0) .
    * @param distance   Distance (in inches) to move from current position.  Negative distance means move backward.
    * @param heading      Absolute Heading Angle (in Degrees) relative to last gyro reset.
    *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
    *                   If a relative angle is required, add/subtract from the current robotHeading.
    */
    public void driveStraight(double maxDriveSpeed,
                              final double distance,
                              final double heading) {

        // Ensure that the OpMode is still active
        if (this.opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            final int moveCounts = (int)(distance * COUNTS_PER_INCH);
	        this.leftTarget = this.leftDrive.getCurrentPosition() + moveCounts;
	        this.rightTarget = this.rightDrive.getCurrentPosition() + moveCounts;

            // Set Target FIRST, then turn on RUN_TO_POSITION
	        this.leftDrive.setTargetPosition(this.leftTarget);
	        this.rightDrive.setTargetPosition(this.rightTarget);

	        this.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	        this.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Set the required driving speed  (must be positive for RUN_TO_POSITION)
            // Start driving straight, and then enter the control loop
            maxDriveSpeed = Math.abs(maxDriveSpeed);
	        this.moveRobot(maxDriveSpeed, 0);

            // keep looping while we are still active, and BOTH motors are running.
            while (this.opModeIsActive() &&
                   (this.leftDrive.isBusy() && this.rightDrive.isBusy())) {

                // Determine required steering to keep on heading
	            this.turnSpeed = this.getSteeringCorrection(heading, P_DRIVE_GAIN);

                // if driving in reverse, the motor correction also needs to be reversed
                if (0 > distance) this.turnSpeed *= -1.0;

                // Apply the turning correction to the current driving speed.
	            this.moveRobot(this.driveSpeed, this.turnSpeed);

                // Display drive status for the driver.
	            this.sendTelemetry(true);
            }

            // Stop all motion & Turn off RUN_TO_POSITION
	        this.moveRobot(0, 0);
	        this.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
	        this.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    /**
     *  Spin on the central axis to point in a new direction.
     *  <p>
     *  Move will stop if either of these conditions occur:
     *  <p>
     *  1) Move gets to the heading (angle)
     *  <p>
     *  2) Driver stops the OpMode running.
     *
     * @param maxTurnSpeed Desired MAX speed of turn. (range 0 to +1.0)
     * @param heading Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *              0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *              If a relative angle is required, add/subtract from current heading.
     */
    public void turnToHeading(final double maxTurnSpeed, final double heading) {

        // Run getSteeringCorrection() once to pre-calculate the current error
	    this.getSteeringCorrection(heading, P_DRIVE_GAIN);

        // keep looping while we are still active, and not on heading.
        while (this.opModeIsActive() && (HEADING_THRESHOLD < Math.abs(headingError))) {

            // Determine required steering to keep on heading
	        this.turnSpeed = this.getSteeringCorrection(heading, P_TURN_GAIN);

            // Clip the speed to the maximum permitted value.
	        this.turnSpeed = Range.clip(this.turnSpeed, -maxTurnSpeed, maxTurnSpeed);

            // Pivot in place by applying the turning correction
	        this.moveRobot(0, this.turnSpeed);

            // Display drive status for the driver.
	        this.sendTelemetry(false);
        }

        // Stop all motion;
	    this.moveRobot(0, 0);
    }

    /**
     *  Obtain & hold a heading for a finite amount of time
     *  <p>
     *  Move will stop once the requested time has elapsed
     *  <p>
     *  This function is useful for giving the robot a moment to stabilize its heading between movements.
     *
     * @param maxTurnSpeed      Maximum differential turn speed (range 0 to +1.0)
     * @param heading    Absolute Heading Angle (in Degrees) relative to last gyro reset.
     *                   0 = fwd. +ve is CCW from fwd. -ve is CW from forward.
     *                   If a relative angle is required, add/subtract from current heading.
     * @param holdTime   Length of time (in seconds) to hold the specified heading.
     */
    public void holdHeading(final double maxTurnSpeed, final double heading, final double holdTime) {

        final ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();

        // keep looping while we have time remaining.
        while (this.opModeIsActive() && (holdTimer.time() < holdTime)) {
            // Determine required steering to keep on heading
	        this.turnSpeed = this.getSteeringCorrection(heading, P_TURN_GAIN);

            // Clip the speed to the maximum permitted value.
	        this.turnSpeed = Range.clip(this.turnSpeed, -maxTurnSpeed, maxTurnSpeed);

            // Pivot in place by applying the turning correction
	        this.moveRobot(0, this.turnSpeed);

            // Display drive status for the driver.
	        this.sendTelemetry(false);
        }

        // Stop all motion;
	    this.moveRobot(0, 0);
    }

    // **********  LOW Level driving functions.  ********************

    /**
     * Use a Proportional Controller to determine how much steering correction is required.
     *
     * @param desiredHeading        The desired absolute heading (relative to last heading reset)
     * @param proportionalGain      Gain factor applied to heading error to obtain turning power.
     * @return                      Turning power needed to get to required heading.
     */
    public double getSteeringCorrection(final double desiredHeading, final double proportionalGain) {
	    this.targetHeading = desiredHeading;  // Save for telemetry

        // Determine the heading current error
	    this.headingError = this.targetHeading - this.getHeading();

        // Normalize the error to be within +/- 180 degrees
        while (180 < headingError) this.headingError -= 360;
        while (- 180 >= headingError) this.headingError += 360;

        // Multiply the error by the gain to determine the required steering correction/  Limit the result to +/- 1.0
        return Range.clip(this.headingError * proportionalGain, -1, 1);
    }

    /**
     * Take separate drive (fwd/rev) and turn (right/left) requests,
     * combines them, and applies the appropriate speed commands to the left and right wheel motors.
     * @param drive forward motor speed
     * @param turn  clockwise turning motor speed.
     */
    public void moveRobot(final double drive, final double turn) {
	    this.driveSpeed = drive;     // save this value as a class member so it can be used by telemetry.
	    this.turnSpeed = turn;      // save this value as a class member so it can be used by telemetry.

	    this.leftSpeed = drive - turn;
	    this.rightSpeed = drive + turn;

        // Scale speeds down if either one exceeds +/- 1.0;
        final double max = Math.max(Math.abs(this.leftSpeed), Math.abs(this.rightSpeed));
        if (1.0 < max)
        {
	        this.leftSpeed /= max;
	        this.rightSpeed /= max;
        }

	    this.leftDrive.setPower(this.leftSpeed);
	    this.rightDrive.setPower(this.rightSpeed);
    }

    /**
     *  Display the various control parameters while driving
     *
     * @param straight  Set to true if we are driving straight, and the encoder positions should be included in the telemetry.
     */
    private void sendTelemetry(final boolean straight) {

        if (straight) {
	        this.telemetry.addData("Motion", "Drive Straight");
	        this.telemetry.addData("Target Pos L:R",  "%7d:%7d", this.leftTarget, this.rightTarget);
	        this.telemetry.addData("Actual Pos L:R",  "%7d:%7d", this.leftDrive.getCurrentPosition(), this.rightDrive.getCurrentPosition());
        } else {
	        this.telemetry.addData("Motion", "Turning");
        }

	    this.telemetry.addData("Heading- Target : Current", "%5.2f : %5.0f", this.targetHeading, this.getHeading());
	    this.telemetry.addData("Error  : Steer Pwr",  "%5.1f : %5.1f", this.headingError, this.turnSpeed);
	    this.telemetry.addData("Wheel Speeds L : R", "%5.2f : %5.2f", this.leftSpeed, this.rightSpeed);
	    this.telemetry.update();
    }

    /**
     * read the Robot heading directly from the IMU (in degrees)
     */
    public double getHeading() {
        final YawPitchRollAngles orientation = this.imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}
