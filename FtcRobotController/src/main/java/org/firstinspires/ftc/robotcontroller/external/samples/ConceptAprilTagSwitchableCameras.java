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
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.CameraState;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/*
 * This OpMode illustrates the basics of AprilTag recognition and pose estimation, using
 * two webcams.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp(name = "Concept: AprilTag Switchable Cameras", group = "Concept")
@Disabled
public class ConceptAprilTagSwitchableCameras extends LinearOpMode {

    /*
     * Variables used for switching cameras.
     */
    private WebcamName webcam1, webcam2;
    private boolean oldLeftBumper;
    private boolean oldRightBumper;

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    @Override
    public void runOpMode() {

	    this.initAprilTag();

        // Wait for the DS start button to be touched.
	    this.telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
	    this.telemetry.addData(">", "Touch START to start OpMode");
	    this.telemetry.update();
	    this.waitForStart();

        if (this.opModeIsActive()) {
            while (this.opModeIsActive()) {

	            this.telemetryCameraSwitching();
	            this.telemetryAprilTag();

                // Push telemetry to the Driver Station.
	            this.telemetry.update();

                // Save CPU resources; can resume streaming when needed.
                if (this.gamepad1.dpad_down) {
	                this.visionPortal.stopStreaming();
                } else if (this.gamepad1.dpad_up) {
	                this.visionPortal.resumeStreaming();
                }

	            this.doCameraSwitching();

                // Share the CPU.
	            this.sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
	    this.visionPortal.close();

    }   // end runOpMode()

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor by using a builder.
	    this.aprilTag = new AprilTagProcessor.Builder().build();

	    this.webcam1 = this.hardwareMap.get(WebcamName.class, "Webcam 1");
	    this.webcam2 = this.hardwareMap.get(WebcamName.class, "Webcam 2");
        final CameraName switchableCamera = ClassFactory.getInstance()
            .getCameraManager().nameForSwitchableCamera(this.webcam1, this.webcam2);

        // Create the vision portal by using a builder.
	    this.visionPortal = new VisionPortal.Builder()
            .setCamera(switchableCamera)
            .addProcessor(this.aprilTag)
            .build();

    }   // end method initAprilTag()

    /**
     * Add telemetry about camera switching.
     */
    private void telemetryCameraSwitching() {

        if (this.visionPortal.getActiveCamera().equals(this.webcam1)) {
	        this.telemetry.addData("activeCamera", "Webcam 1");
	        this.telemetry.addData("Press RightBumper", "to switch to Webcam 2");
        } else {
	        this.telemetry.addData("activeCamera", "Webcam 2");
	        this.telemetry.addData("Press LeftBumper", "to switch to Webcam 1");
        }

    }   // end method telemetryCameraSwitching()

    /**
     * Add telemetry about AprilTag detections.
     */
    private void telemetryAprilTag() {

        final List<AprilTagDetection> currentDetections = this.aprilTag.getDetections();
	    this.telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (final AprilTagDetection detection : currentDetections) {
            if (null != detection.metadata) {
	            this.telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
	            this.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
	            this.telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
	            this.telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
	            this.telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
	            this.telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
	    this.telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
	    this.telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
	    this.telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

    /**
     * Set the active camera according to input from the gamepad.
     */
    private void doCameraSwitching() {
        if (CameraState.STREAMING == visionPortal.getCameraState()) {
            // If the left bumper is pressed, use Webcam 1.
            // If the right bumper is pressed, use Webcam 2.
            final boolean newLeftBumper  = this.gamepad1.left_bumper;
            final boolean newRightBumper = this.gamepad1.right_bumper;
            if (newLeftBumper && ! this.oldLeftBumper) {
	            this.visionPortal.setActiveCamera(this.webcam1);
            } else if (newRightBumper && ! this.oldRightBumper) {
	            this.visionPortal.setActiveCamera(this.webcam2);
            }
	        this.oldLeftBumper = newLeftBumper;
	        this.oldRightBumper = newRightBumper;
        }

    }   // end method doCameraSwitching()

}   // end class
