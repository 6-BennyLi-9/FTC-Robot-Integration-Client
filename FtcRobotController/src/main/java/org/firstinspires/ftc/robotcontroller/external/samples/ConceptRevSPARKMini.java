/* Copyright (c) 2018 FIRST. All rights reserved.
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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/*
 * This OpMode demonstrates a POV Drive system, with commented-out code for a Tank Drive system,
 * for a two wheeled robot using two REV SPARKminis.
 * To use this example, connect two REV SPARKminis into servo ports on the Control Hub. On the
 * robot configuration, use the drop down list under 'Servos' to select 'REV SPARKmini Controller'
 * and name them 'left_drive' and 'right_drive'.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="REV SPARKmini Simple Drive Example", group="Concept")
@Disabled
public class ConceptRevSPARKMini extends LinearOpMode {

    // Declare OpMode members.
    private final ElapsedTime   runtime   = new ElapsedTime();
    private       DcMotorSimple leftDrive;
    private DcMotorSimple rightDrive;

    @Override
    public void runOpMode() {
	    this.telemetry.addData("Status", "Initialized");
	    this.telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during robot configuration.
	    this.leftDrive = this.hardwareMap.get(DcMotorSimple.class, "left_drive");
	    this.rightDrive = this.hardwareMap.get(DcMotorSimple.class, "right_drive");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backward when connected directly to the battery
	    this.leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
	    this.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start (driver presses START)
	    this.waitForStart();
	    this.runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (this.opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            final double leftPower;
            final double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            final double drive = - this.gamepad1.left_stick_y;
            final double turn  = this.gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
	        this.leftDrive.setPower(leftPower);
	        this.rightDrive.setPower(rightPower);

            // Show the elapsed game time and wheel power.
	        this.telemetry.addData("Status", "Run Time: " + this.runtime);
	        this.telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
	        this.telemetry.update();
        }
    }
}
