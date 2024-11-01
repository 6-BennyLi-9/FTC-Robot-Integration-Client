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
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * This OpMode determines the best Exposure for minimizing image motion-blur on a Webcam
 * Note that it is not possible to control the exposure for a Phone Camera, so if you are using a Phone for the Robot Controller
 * this OpMode/Feature only applies to an externally connected Webcam
 *
 * The goal is to determine the smallest (shortest) Exposure value that still provides reliable Tag Detection.
 * Starting with the minimum Exposure and maximum Gain, the exposure is slowly increased until the Tag is
 * detected reliably from the likely operational distance.
 *
 *
 * The best way to run this optimization is to view the camera preview screen while changing the exposure and gains.
 *
 * To do this, you need to view the RobotController screen directly (not from Driver Station)
 * This can be done directly from a RC phone screen (if you are using an external Webcam), but for a Control Hub you must either plug an
 * HDMI monitor into the Control Hub HDMI port, or use an external viewer program like ScrCpy (https://scrcpy.org/)
 *
 * Use Android Studio to Copy this Class, and Paste it into the TeamCode/src/main/java/org/firstinspires/ftc/teamcode folder.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */

@TeleOp(name="Optimize AprilTag Exposure", group = "Concept")
@Disabled
public class ConceptAprilTagOptimizeExposure extends LinearOpMode
{
    private VisionPortal visionPortal;        // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private int     myExposure  ;
    private int     minExposure ;
    private int     maxExposure ;
    private int     myGain      ;
    private int     minGain ;
    private int     maxGain ;

    boolean thisExpUp;
    boolean thisExpDn;
    boolean thisGainUp;
    boolean thisGainDn;

    boolean lastExpUp;
    boolean lastExpDn;
    boolean lastGainUp;
    boolean lastGainDn;
    @Override public void runOpMode()
    {
        // Initialize the Apriltag Detection process
	    this.initAprilTag();

        // Establish Min and Max Gains and Exposure.  Then set a low exposure with high gain
	    this.getCameraSetting();
	    this.myExposure = Math.min(5, this.minExposure);
	    this.myGain = this.maxGain;
	    this.setManualExposure(this.myExposure, this.myGain);

        // Wait for the match to begin.
	    this.telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
	    this.telemetry.addData(">", "Touch START to start OpMode");
	    this.telemetry.update();
	    this.waitForStart();

        while (this.opModeIsActive())
        {
	        this.telemetry.addLine("Find lowest Exposure that gives reliable detection.");
	        this.telemetry.addLine("Use Left bump/trig to adjust Exposure.");
	        this.telemetry.addLine("Use Right bump/trig to adjust Gain.\n");

            // Display how many Tags Detected
            final List<AprilTagDetection> currentDetections = this.aprilTag.getDetections();
            final int                     numTags           = currentDetections.size();
            if (0 < numTags) this.telemetry.addData("Tag", "####### %d Detected  ######", currentDetections.size());
            else this.telemetry.addData("Tag", "----------- none - ----------");

	        this.telemetry.addData("Exposure","%d  (%d - %d)", this.myExposure, this.minExposure, this.maxExposure);
	        this.telemetry.addData("Gain","%d  (%d - %d)", this.myGain, this.minGain, this.maxGain);
	        this.telemetry.update();

            // check to see if we need to change exposure or gain.
	        this.thisExpUp = this.gamepad1.left_bumper;
	        this.thisExpDn = 0.25 < gamepad1.left_trigger;
	        this.thisGainUp = this.gamepad1.right_bumper;
	        this.thisGainDn = 0.25 < gamepad1.right_trigger;

            // look for clicks to change exposure
            if (this.thisExpUp && ! this.lastExpUp) {
	            this.myExposure = Range.clip(this.myExposure + 1, this.minExposure, this.maxExposure);
	            this.setManualExposure(this.myExposure, this.myGain);
            } else if (this.thisExpDn && ! this.lastExpDn) {
	            this.myExposure = Range.clip(this.myExposure - 1, this.minExposure, this.maxExposure);
	            this.setManualExposure(this.myExposure, this.myGain);
            }

            // look for clicks to change the gain
            if (this.thisGainUp && ! this.lastGainUp) {
	            this.myGain = Range.clip(this.myGain + 1, this.minGain, this.maxGain);
	            this.setManualExposure(this.myExposure, this.myGain);
            } else if (this.thisGainDn && ! this.lastGainDn) {
	            this.myGain = Range.clip(this.myGain - 1, this.minGain, this.maxGain);
	            this.setManualExposure(this.myExposure, this.myGain);
            }

	        this.lastExpUp = this.thisExpUp;
	        this.lastExpDn = this.thisExpDn;
	        this.lastGainUp = this.thisGainUp;
	        this.lastGainDn = this.thisGainDn;

	        this.sleep(20);
        }
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
	    this.aprilTag = new AprilTagProcessor.Builder().build();

        // Create the WEBCAM vision portal by using a builder.
	    this.visionPortal = new VisionPortal.Builder()
                .setCamera(this.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(this.aprilTag)
                .build();
    }

    /*
        Manually set the camera gain and exposure.
        Can only be called AFTER calling initAprilTag();
        Returns true if controls are set.
     */
    private boolean    setManualExposure(final int exposureMS, final int gain) {
        // Ensure Vision Portal has been setup.
        if (null == visionPortal) {
            return false;
        }

        // Wait for the camera to be open
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
            // Set exposure.  Make sure we are in Manual Mode for these values to take effect.
            final ExposureControl exposureControl = this.visionPortal.getCameraControl(ExposureControl.class);
            if (ExposureControl.Mode.Manual != exposureControl.getMode()) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
	            this.sleep(50);
            }
            exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
	        this.sleep(20);

            // Set Gain.
            final GainControl gainControl = this.visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
	        this.sleep(20);
            return (true);
        } else {
            return (false);
        }
    }

    /*
        Read this camera's minimum and maximum Exposure and Gain settings.
        Can only be called AFTER calling initAprilTag();
     */
    private void getCameraSetting() {
        // Ensure Vision Portal has been setup.
        if (null == visionPortal) {
            return;
        }

        // Wait for the camera to be open
        if (VisionPortal.CameraState.STREAMING != visionPortal.getCameraState()) {
	        this.telemetry.addData("Camera", "Waiting");
	        this.telemetry.update();
            while (! this.isStopRequested() && (VisionPortal.CameraState.STREAMING != visionPortal.getCameraState())) {
	            this.sleep(20);
            }
	        this.telemetry.addData("Camera", "Ready");
	        this.telemetry.update();
        }

        // Get camera control values unless we are stopping.
        if (! this.isStopRequested()) {
            final ExposureControl exposureControl = this.visionPortal.getCameraControl(ExposureControl.class);
	        this.minExposure = (int)exposureControl.getMinExposure(TimeUnit.MILLISECONDS) + 1;
	        this.maxExposure = (int)exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);

            final GainControl gainControl = this.visionPortal.getCameraControl(GainControl.class);
	        this.minGain = gainControl.getMinGain();
	        this.maxGain = gainControl.getMaxGain();
        }
    }
}
