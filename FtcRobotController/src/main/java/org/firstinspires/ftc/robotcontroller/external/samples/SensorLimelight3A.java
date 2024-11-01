/*
Copyright (c) 2024 Limelight Vision

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of FIRST nor the names of its contributors may be used to
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

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

/*
 * This OpMode illustrates how to use the Limelight3A Vision Sensor.
 *
 * @see <a href="https://limelightvision.io/">Limelight</a>
 *
 * Notes on configuration:
 *
 *   The device presents itself, when plugged into a USB port on a Control Hub as an ethernet
 *   interface.  A DHCP server running on the Limelight automatically assigns the Control Hub an
 *   ip address for the new ethernet interface.
 *
 *   Since the Limelight is plugged into a USB port, it will be listed on the top level configuration
 *   activity along with the Control Hub Portal and other USB devices such as webcams.  Typically
 *   serial numbers are displayed below the device's names.  In the case of the Limelight device, the
 *   Control Hub's assigned ip address for that ethernet interface is used as the "serial number".
 *
 *   Tapping the Limelight's name, transitions to a new screen where the user can rename the Limelight
 *   and specify the Limelight's ip address.  Users should take care not to confuse the ip address of
 *   the Limelight itself, which can be configured through the Limelight settings page via a web browser,
 *   and the ip address the Limelight device assigned the Control Hub and which is displayed in small text
 *   below the name of the Limelight on the top level configuration screen.
 */
@TeleOp(name = "Sensor: Limelight3A", group = "Sensor")
@Disabled
public class SensorLimelight3A extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException
    {
	    this.limelight = this.hardwareMap.get(Limelight3A.class, "limelight");

	    this.telemetry.setMsTransmissionInterval(11);

	    this.limelight.pipelineSwitch(0);

        /*
         * Starts polling for data.  If you neglect to call start(), getLatestResult() will return null.
         */
	    this.limelight.start();

	    this.telemetry.addData(">", "Robot Ready.  Press Play.");
	    this.telemetry.update();
	    this.waitForStart();

        while (this.opModeIsActive()) {
            final LLStatus status = this.limelight.getStatus();
	        this.telemetry.addData("Name", "%s",
                    status.getName());
	        this.telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(),(int)status.getFps());
	        this.telemetry.addData("Pipeline", "Index: %d, Type: %s",
                    status.getPipelineIndex(), status.getPipelineType());

            final LLResult result = this.limelight.getLatestResult();
            if (null != result) {
                // Access general information
                final Pose3D botpose        = result.getBotpose();
                final double captureLatency = result.getCaptureLatency();
                final double targetingLatency = result.getTargetingLatency();
                final double parseLatency     = result.getParseLatency();
	            this.telemetry.addData("LL Latency", captureLatency + targetingLatency);
	            this.telemetry.addData("Parse Latency", parseLatency);
	            this.telemetry.addData("PythonOutput", java.util.Arrays.toString(result.getPythonOutput()));
                
                if (result.isValid()) {
	                this.telemetry.addData("tx", result.getTx());
	                this.telemetry.addData("txnc", result.getTxNC());
	                this.telemetry.addData("ty", result.getTy());
	                this.telemetry.addData("tync", result.getTyNC());

	                this.telemetry.addData("Botpose", botpose.toString());

                    // Access barcode results
                    final List<LLResultTypes.BarcodeResult> barcodeResults = result.getBarcodeResults();
                    for (final LLResultTypes.BarcodeResult br : barcodeResults) {
	                    this.telemetry.addData("Barcode", "Data: %s", br.getData());
                    }

                    // Access classifier results
                    final List<LLResultTypes.ClassifierResult> classifierResults = result.getClassifierResults();
                    for (final LLResultTypes.ClassifierResult cr : classifierResults) {
	                    this.telemetry.addData("Classifier", "Class: %s, Confidence: %.2f", cr.getClassName(), cr.getConfidence());
                    }

                    // Access detector results
                    final List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
                    for (final LLResultTypes.DetectorResult dr : detectorResults) {
	                    this.telemetry.addData("Detector", "Class: %s, Area: %.2f", dr.getClassName(), dr.getTargetArea());
                    }

                    // Access fiducial results
                    final List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                    for (final LLResultTypes.FiducialResult fr : fiducialResults) {
	                    this.telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(),fr.getTargetXDegrees(), fr.getTargetYDegrees());
                    }

                    // Access color results
                    final List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                    for (final LLResultTypes.ColorResult cr : colorResults) {
	                    this.telemetry.addData("Color", "X: %.2f, Y: %.2f", cr.getTargetXDegrees(), cr.getTargetYDegrees());
                    }
                }
            } else {
	            this.telemetry.addData("Limelight", "No data available");
            }

	        this.telemetry.update();
        }
	    this.limelight.stop();
    }
}
