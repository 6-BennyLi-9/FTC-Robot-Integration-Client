/*
Copyright (c) 2022 REV Robotics, FIRST

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of REV Robotics nor the names of its contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

/*
 * This OpMode demonstrates the impact of setting the IMU orientation correctly or incorrectly. This
 * code assumes there is an IMU configured with the name "imu".
 *
 * Note: This OpMode is more of a tool than a code sample. The User Interface portion of this code
 *       goes beyond simply showing how to interface to the IMU.<br>
 *       For a minimal example of interfacing to an IMU, please see the SensorIMUOrthogonal or SensorIMUNonOrthogonal sample OpModes.
 *
 * This OpMode enables you to re-specify the Hub Mounting orientation dynamically by using gamepad controls.
 * While doing so, the sample will display how Pitch, Roll and Yaw angles change as the hub is moved.
 *
 * The gamepad controls let you change the two parameters that specify how the Control/Expansion Hub is mounted. <br>
 * The first parameter specifies which direction the printed logo on the Hub is pointing. <br>
 * The second parameter specifies which direction the USB connector on the Hub is pointing. <br>
 * All directions are relative to the robot, and left/right is as viewed from behind the robot.
 *
 * How will you know if you have chosen the correct Orientation? With the correct orientation
 * parameters selected, pitch/roll/yaw should act as follows:
 *
 *   Pitch value should INCREASE as the robot is tipped UP at the front. (Rotation about X) <br>
 *   Roll value should INCREASE as the robot is tipped UP at the left side. (Rotation about Y) <br>
 *   Yaw value should INCREASE as the robot is rotated Counter Clockwise. (Rotation about Z) <br>
 *
 * The Yaw can be reset (to zero) by pressing the Y button on the gamepad (Triangle on a PS4 controller)
 *
 * The rotational velocities should follow the change in corresponding axes.
 */

@TeleOp(name="Concept: IMU Orientation", group="Concept")
@Disabled
public class ConceptExploringIMUOrientation extends LinearOpMode {
    static RevHubOrientationOnRobot.LogoFacingDirection[] logoFacingDirections
            = RevHubOrientationOnRobot.LogoFacingDirection.values();
    static RevHubOrientationOnRobot.UsbFacingDirection[] usbFacingDirections
            = RevHubOrientationOnRobot.UsbFacingDirection.values();
    static int LAST_DIRECTION = logoFacingDirections.length - 1;
    static float TRIGGER_THRESHOLD = 0.2f;

    IMU imu;
    int logoFacingDirectionPosition;
    int usbFacingDirectionPosition;
    boolean orientationIsValid = true;

    @Override public void runOpMode() throws InterruptedException {
	    this.imu = this.hardwareMap.get(IMU.class, "imu");
	    this.logoFacingDirectionPosition = 0; // Up
	    this.usbFacingDirectionPosition = 2; // Forward

	    this.updateOrientation();

        boolean justChangedLogoDirection = false;
        boolean justChangedUsbDirection = false;

        // Loop until stop requested
        while (! this.isStopRequested()) {

            // Check to see if Yaw reset is requested (Y button)
            if (this.gamepad1.y) {
	            this.telemetry.addData("Yaw", "Resetting\n");
	            this.imu.resetYaw();
            } else {
	            this.telemetry.addData("Yaw", "Press Y (triangle) on Gamepad to reset.\n");
            }

            // Check to see if new Logo Direction is requested
            if (this.gamepad1.left_bumper || this.gamepad1.right_bumper) {
                if (!justChangedLogoDirection) {
                    justChangedLogoDirection = true;
                    if (this.gamepad1.left_bumper) {
	                    this.logoFacingDirectionPosition--;
                        if (0 > logoFacingDirectionPosition) {
	                        this.logoFacingDirectionPosition = LAST_DIRECTION;
                        }
                    } else {
	                    this.logoFacingDirectionPosition++;
                        if (this.logoFacingDirectionPosition > LAST_DIRECTION) {
	                        this.logoFacingDirectionPosition = 0;
                        }
                    }
	                this.updateOrientation();
                }
            } else {
                justChangedLogoDirection = false;
            }

            // Check to see if new USB Direction is requested
            if (this.gamepad1.left_trigger > TRIGGER_THRESHOLD || this.gamepad1.right_trigger > TRIGGER_THRESHOLD) {
                if (!justChangedUsbDirection) {
                    justChangedUsbDirection = true;
                    if (this.gamepad1.left_trigger > TRIGGER_THRESHOLD) {
	                    this.usbFacingDirectionPosition--;
                        if (0 > usbFacingDirectionPosition) {
	                        this.usbFacingDirectionPosition = LAST_DIRECTION;
                        }
                    } else {
	                    this.usbFacingDirectionPosition++;
                        if (this.usbFacingDirectionPosition > LAST_DIRECTION) {
	                        this.usbFacingDirectionPosition = 0;
                        }
                    }
	                this.updateOrientation();
                }
            } else {
                justChangedUsbDirection = false;
            }

            // Display User instructions and IMU data
	        this.telemetry.addData("logo Direction (set with bumpers)", logoFacingDirections[this.logoFacingDirectionPosition]);
	        this.telemetry.addData("usb Direction (set with triggers)", usbFacingDirections[this.usbFacingDirectionPosition] + "\n");

            if (this.orientationIsValid) {
                final YawPitchRollAngles orientation     = this.imu.getRobotYawPitchRollAngles();
                final AngularVelocity    angularVelocity = this.imu.getRobotAngularVelocity(AngleUnit.DEGREES);

	            this.telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", orientation.getYaw(AngleUnit.DEGREES));
	            this.telemetry.addData("Pitch (X)", "%.2f Deg.", orientation.getPitch(AngleUnit.DEGREES));
	            this.telemetry.addData("Roll (Y)", "%.2f Deg.\n", orientation.getRoll(AngleUnit.DEGREES));
	            this.telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", angularVelocity.zRotationRate);
	            this.telemetry.addData("Pitch (X) velocity", "%.2f Deg/Sec", angularVelocity.xRotationRate);
	            this.telemetry.addData("Roll (Y) velocity", "%.2f Deg/Sec", angularVelocity.yRotationRate);
            } else {
	            this.telemetry.addData("Error", "Selected orientation on robot is invalid");
            }

	        this.telemetry.update();
        }
    }

    // apply any requested orientation changes.
    void updateOrientation() {
        final RevHubOrientationOnRobot.LogoFacingDirection logo = logoFacingDirections[this.logoFacingDirectionPosition];
        final RevHubOrientationOnRobot.UsbFacingDirection  usb  = usbFacingDirections[this.usbFacingDirectionPosition];
        try {
            final RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logo, usb);
	        this.imu.initialize(new IMU.Parameters(orientationOnRobot));
	        this.orientationIsValid = true;
        } catch (final IllegalArgumentException e) {
	        this.orientationIsValid = false;
        }
    }
}
