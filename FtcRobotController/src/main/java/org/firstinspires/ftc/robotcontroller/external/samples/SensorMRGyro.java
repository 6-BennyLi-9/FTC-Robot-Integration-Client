/* Copyright (c) 2017 FIRST. All rights reserved.
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

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/*
 * This OpMode shows how to use the Modern Robotics Gyro.
 *
 * The OpMode assumes that the gyro sensor is attached to a Device Interface Module
 * I2C channel and is configured with a name of "gyro".
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
*/
@TeleOp(name = "Sensor: MR Gyro", group = "Sensor")
@Disabled
public class SensorMRGyro extends LinearOpMode {

  /* In this sample, for illustration purposes we use two interfaces on the one gyro object.
   * That's likely atypical: you'll probably use one or the other in any given situation,
   * depending on what you're trying to do. {@link IntegratingGyroscope} (and it's base interface,
   * {@link Gyroscope}) are common interfaces supported by possibly several different gyro
   * implementations. {@link ModernRoboticsI2cGyro}, by contrast, provides functionality that
   * is unique to the Modern Robotics gyro sensor.
   */
  IntegratingGyroscope gyro;
  ModernRoboticsI2cGyro modernRoboticsI2cGyro;

  // A timer helps provide feedback while calibration is taking place
  ElapsedTime timer = new ElapsedTime();

  @Override
  public void runOpMode() {

    boolean lastResetState = false;
    boolean curResetState  = false;

    // Get a reference to a Modern Robotics gyro object. We use several interfaces
    // on this object to illustrate which interfaces support which functionality.
	  this.modernRoboticsI2cGyro = this.hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
	  this.gyro = this.modernRoboticsI2cGyro;
    // If you're only interested int the IntegratingGyroscope interface, the following will suffice.
    // gyro = hardwareMap.get(IntegratingGyroscope.class, "gyro");
    // A similar approach will work for the Gyroscope interface, if that's all you need.

    // Start calibrating the gyro. This takes a few seconds and is worth performing
    // during the initialization phase at the start of each OpMode.
	  this.telemetry.log().add("Gyro Calibrating. Do Not Move!");
	  this.modernRoboticsI2cGyro.calibrate();

    // Wait until the gyro calibration is complete
	  this.timer.reset();
    while (! this.isStopRequested() && this.modernRoboticsI2cGyro.isCalibrating())  {
	    this.telemetry.addData("calibrating", "%s", 0 == Math.round(timer.seconds()) % 2 ? "|.." : "..|");
	    this.telemetry.update();
	    this.sleep(50);
    }

	  this.telemetry.log().clear();
	  this.telemetry.log().add("Gyro Calibrated. Press Start.");
	  this.telemetry.clear();
	  this.telemetry.update();

    // Wait for the start button to be pressed
	  this.waitForStart();
	  this.telemetry.log().clear();
	  this.telemetry.log().add("Press A & B to reset heading");

    // Loop until we're asked to stop
    while (this.opModeIsActive())  {

      // If the A and B buttons are pressed just now, reset Z heading.
      curResetState = (this.gamepad1.a && this.gamepad1.b);
      if (curResetState && !lastResetState) {
	      this.modernRoboticsI2cGyro.resetZAxisIntegrator();
      }
      lastResetState = curResetState;

      // The raw() methods report the angular rate of change about each of the
      // three axes directly as reported by the underlying sensor IC.
      final int rawX = this.modernRoboticsI2cGyro.rawX();
      final int rawY = this.modernRoboticsI2cGyro.rawY();
      final int rawZ = this.modernRoboticsI2cGyro.rawZ();
      final int heading = this.modernRoboticsI2cGyro.getHeading();
      final int integratedZ = this.modernRoboticsI2cGyro.getIntegratedZValue();

      // Read dimensionalized data from the gyro. This gyro can report angular velocities
      // about all three axes. Additionally, it internally integrates the Z axis to
      // be able to report an absolute angular Z orientation.
      final AngularVelocity rates  = this.gyro.getAngularVelocity(AngleUnit.DEGREES);
      final float           zAngle = this.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

      // Read administrative information from the gyro
      final int zAxisOffset             = this.modernRoboticsI2cGyro.getZAxisOffset();
      final int zAxisScalingCoefficient = this.modernRoboticsI2cGyro.getZAxisScalingCoefficient();

	    this.telemetry.addLine()
        .addData("dx", this.formatRate(rates.xRotationRate))
        .addData("dy", this.formatRate(rates.yRotationRate))
        .addData("dz", "%s deg/s", this.formatRate(rates.zRotationRate));
	    this.telemetry.addData("angle", "%s deg", this.formatFloat(zAngle));
	    this.telemetry.addData("heading", "%3d deg", heading);
	    this.telemetry.addData("integrated Z", "%3d", integratedZ);
	    this.telemetry.addLine()
        .addData("rawX", this.formatRaw(rawX))
        .addData("rawY", this.formatRaw(rawY))
        .addData("rawZ", this.formatRaw(rawZ));
	    this.telemetry.addLine().addData("z offset", zAxisOffset).addData("z coeff", zAxisScalingCoefficient);
	    this.telemetry.update();
    }
  }

  String formatRaw(final int rawValue) {
    return String.format("%d", rawValue);
  }

  String formatRate(final float rate) {
    return String.format("%.3f", rate);
  }

  String formatFloat(final float rate) {
    return String.format("%.3f", rate);
  }

}
