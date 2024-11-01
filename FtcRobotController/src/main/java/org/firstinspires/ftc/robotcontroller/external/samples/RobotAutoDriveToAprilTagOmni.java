/* Copyright (c) 2023 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * This OpMode illustrates using a camera to locate and drive towards a specific AprilTag.
 * The code assumes a Holonomic (Mecanum or X Drive) Robot.
 *
 * For an introduction to AprilTags, see the ftc-docs link below:
 * https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
 *
 * When an AprilTag in the TagLibrary is detected, the SDK provides location and orientation of the tag, relative to the camera.
 * This information is provided in the "ftcPose" member of the returned "detection", and is explained in the ftc-docs page linked below.
 * https://ftc-docs.firstinspires.org/apriltag-detection-values
 *
 * The drive goal is to rotate to keep the Tag centered in the camera, while strafing to be directly in front of the tag, and
 * driving towards the tag to achieve the desired distance.
 * To reduce any motion blur (which will interrupt the detection process) the Camera exposure is reduced to a very low value (5mS)
 * You can determine the best Exposure and Gain values by using the ConceptAprilTagOptimizeExposure OpMode in this Samples folder.
 *
 * The code assumes a Robot Configuration with motors named: leftfront_drive and rightfront_drive, leftback_drive and rightback_drive.
 * The motor directions must be set so a positive power goes forward on all wheels.
 * This sample assumes that the current game AprilTag Library (usually for the current season) is being loaded by default,
 * so you should choose to approach a valid tag ID.
 *
 * Under manual control, the left stick will move forward/back & left/right.  The right stick will rotate the robot.
 * Manually drive the robot until it displays Target data on the Driver Station.
 *
 * Press and hold the *Left Bumper* to enable the automatic "Drive to target" mode.
 * Release the Left Bumper to return to manual driving mode.
 *
 * Under "Drive To Target" mode, the robot has three goals:
 * 1) Turn the robot to always keep the Tag centered on the camera frame. (Use the Target Bearing to turn the robot.)
 * 2) Strafe the robot towards the centerline of the Tag, so it approaches directly in front  of the tag.  (Use the Target Yaw to strafe the robot)
 * 3) Drive towards the Tag to get to the desired distance.  (Use Tag Range to drive the robot forward/backward)
 *
 * Use DESIRED_DISTANCE to set how close you want the robot to get to the target.
 * Speed and Turn sensitivity can be adjusted using the SPEED_GAIN, STRAFE_GAIN and TURN_GAIN constants.
 *
 * Use Android Studio to Copy this Class, and Paste it into the TeamCode/src/main/java/org/firstinspires/ftc/teamcode folder.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 */

@TeleOp(name="Omni Drive To AprilTag", group = "Concept")
@Disabled
public class RobotAutoDriveToAprilTagOmni extends LinearOpMode
{
    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    private DcMotor leftFrontDrive;  //  Used to control the left front drive wheel
    private DcMotor rightFrontDrive;  //  Used to control the right front drive wheel
    private DcMotor leftBackDrive;  //  Used to control the left back drive wheel
    private DcMotor rightBackDrive;  //  Used to control the right back drive wheel

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static final int DESIRED_TAG_ID = -1;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag;     // Used to hold the data for a detected AprilTag

    @Override public void runOpMode()
    {
        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
        double  drive           = 0;        // Desired forward power/speed (-1 to +1)
        double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
        double  turn            = 0;        // Desired turning power/speed (-1 to +1)

        // Initialize the Apriltag Detection process
	    this.initAprilTag();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must match the names assigned during the robot configuration.
        // step (using the FTC Robot Controller app on the phone).
	    this.leftFrontDrive = this.hardwareMap.get(DcMotor.class, "leftfront_drive");
	    this.rightFrontDrive = this.hardwareMap.get(DcMotor.class, "rightfront_drive");
	    this.leftBackDrive = this.hardwareMap.get(DcMotor.class, "leftback_drive");
	    this.rightBackDrive = this.hardwareMap.get(DcMotor.class, "rightback_drive");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
	    this.leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	    this.leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	    this.rightFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
	    this.rightBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        if (RobotAutoDriveToAprilTagOmni.USE_WEBCAM) this.setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
	    this.telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
	    this.telemetry.addData(">", "Touch START to start OpMode");
	    this.telemetry.update();
	    this.waitForStart();

        while (this.opModeIsActive())
        {
            targetFound = false;
	        this.desiredTag = null;

            // Step through the list of detected tags and look for a matching tag
            final List<AprilTagDetection> currentDetections = this.aprilTag.getDetections();
            for (final AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (null != detection.metadata) {
                    //  Check to see if we want to track towards this tag.
                    if ((RobotAutoDriveToAprilTagOmni.DESIRED_TAG_ID < 0) || (DESIRED_TAG_ID == AprilTagDetection.detection.id)) {
                        // Yes, we want to use this tag.
                        targetFound = true;
	                    this.desiredTag = detection;
                        break;  // don't look any further.
                    } else {
                        // This tag is in the library, but we do not want to track it right now.
	                    this.telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                    }
                } else {
                    // This tag is NOT in the library, so we don't have enough information to track to it.
	                this.telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                }
            }

            // Tell the driver what we see, and what to do.
            if (targetFound) {
	            this.telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
	            this.telemetry.addData("Found", "ID %d (%s)", this.desiredTag.id, this.desiredTag.metadata.name);
	            this.telemetry.addData("Range",  "%5.1f inches", this.desiredTag.ftcPose.range);
	            this.telemetry.addData("Bearing","%3.0f degrees", this.desiredTag.ftcPose.bearing);
	            this.telemetry.addData("Yaw","%3.0f degrees", this.desiredTag.ftcPose.yaw);
            } else {
	            this.telemetry.addData("\n>","Drive using joysticks to find valid target\n");
            }

            // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
            if (this.gamepad1.left_bumper && targetFound) {

                // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
                final double rangeError   = (this.desiredTag.ftcPose.range - this.DESIRED_DISTANCE);
                final double headingError = this.desiredTag.ftcPose.bearing;
                final double yawError     = this.desiredTag.ftcPose.yaw;

                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive  = Range.clip(rangeError * this.SPEED_GAIN, - this.MAX_AUTO_SPEED, this.MAX_AUTO_SPEED);
                turn   = Range.clip(headingError * this.TURN_GAIN, - this.MAX_AUTO_TURN, this.MAX_AUTO_TURN) ;
                strafe = Range.clip(-yawError * this.STRAFE_GAIN, - this.MAX_AUTO_STRAFE, this.MAX_AUTO_STRAFE);

	            this.telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            } else {

                // drive using manual POV Joystick mode.  Slow things down to make the robot more controlable.
                drive  = - this.gamepad1.left_stick_y / 2.0;  // Reduce drive rate to 50%.
                strafe = - this.gamepad1.left_stick_x / 2.0;  // Reduce strafe rate to 50%.
                turn   = - this.gamepad1.right_stick_x / 3.0;  // Reduce turn rate to 33%.
	            this.telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            }
	        this.telemetry.update();

            // Apply desired axes motions to the drivetrain.
	        this.moveRobot(drive, strafe, turn);
	        this.sleep(10);
        }
    }

    /**
     * Move robot according to desired axes motions
     * <p>
     * Positive X is forward
     * <p>
     * Positive Y is strafe left
     * <p>
     * Positive Yaw is counter-clockwise
     */
    public void moveRobot(final double x, final double y, final double yaw) {
        // Calculate wheel powers.
        double leftFrontPower    =  x -y -yaw;
        double rightFrontPower   =  x +y +yaw;
        double leftBackPower     =  x +y -yaw;
        double rightBackPower    =  x -y +yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (1.0 < max) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
	    this.leftFrontDrive.setPower(leftFrontPower);
	    this.rightFrontDrive.setPower(rightFrontPower);
	    this.leftBackDrive.setPower(leftBackPower);
	    this.rightBackDrive.setPower(rightBackPower);
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
	    this.aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // e.g. Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
	    this.aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (RobotAutoDriveToAprilTagOmni.USE_WEBCAM) {
	        this.visionPortal = new VisionPortal.Builder()
                    .setCamera(this.hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(this.aprilTag)
                    .build();
        } else {
	        this.visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(this.aprilTag)
                    .build();
        }
    }

    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void    setManualExposure(final int exposureMS, final int gain) {
        // Wait for the camera to be open, then use the controls

        if (null == visionPortal) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (VisionPortal.CameraState.STREAMING != visionPortal.getCameraState()) {
	        this.telemetry.addData("Camera", "Waiting");
	        this.telemetry.update();
            while (! this.isStopRequested() && (VisionPortal.CameraState.STREAMING != visionPortal.getCameraState())) {
	            this.sleep(20);
            }
	        this.telemetry.addData("Camera", "Ready");
	        this.telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (! this.isStopRequested())
        {
            final ExposureControl exposureControl = this.visionPortal.getCameraControl(ExposureControl.class);
            if (ExposureControl.Mode.Manual != exposureControl.getMode()) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
	            this.sleep(50);
            }
            exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
	        this.sleep(20);
            final GainControl gainControl = this.visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
	        this.sleep(20);
        }
    }
}
