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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/*
 * This OpMode executes a Tank Drive control TeleOp a direct drive robot
 * The code is structured as an Iterative OpMode
 *
 * In this mode, the left and right joysticks control the left and right motors respectively.
 * Pushing a joystick forward will make the attached motor drive forward.
 * It raises and lowers the claw using the Gamepad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Robot: Teleop Tank", group="Robot")
@Disabled
public class RobotTeleopTank_Iterative extends OpMode{

    /* Declare OpMode members. */
    public DcMotor  leftDrive;
    public DcMotor  rightDrive;
    public DcMotor  leftArm;
    public Servo    leftClaw;
    public Servo    rightClaw;

    double clawOffset;

    public static final double MID_SERVO   =  0.5 ;
    public static final double CLAW_SPEED  = 0.02 ;        // sets rate to move servo
    public static final double ARM_UP_POWER    =  0.50 ;   // Run arm motor up at 50% power
    public static final double ARM_DOWN_POWER  = -0.25 ;   // Run arm motor down at -25% power

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        // Define and Initialize Motors
	    this.leftDrive = this.hardwareMap.get(DcMotor.class, "left_drive");
	    this.rightDrive = this.hardwareMap.get(DcMotor.class, "right_drive");
	    this.leftArm = this.hardwareMap.get(DcMotor.class, "left_arm");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left and right sticks forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
	    this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	    this.rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
        // leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
	    this.leftClaw = this.hardwareMap.get(Servo.class, "left_hand");
	    this.rightClaw = this.hardwareMap.get(Servo.class, "right_hand");
	    this.leftClaw.setPosition(RobotTeleopTank_Iterative.MID_SERVO);
	    this.rightClaw.setPosition(RobotTeleopTank_Iterative.MID_SERVO);

        // Send telemetry message to signify robot waiting;
	    this.telemetry.addData(">", "Robot Ready.  Press START.");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        final double left;
        final double right;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forward, so negate it)
        left = - this.gamepad1.left_stick_y;
        right = - this.gamepad1.right_stick_y;

	    this.leftDrive.setPower(left);
	    this.rightDrive.setPower(right);

        // Use gamepad left & right Bumpers to open and close the claw
        if (this.gamepad1.right_bumper) this.clawOffset += RobotTeleopTank_Iterative.CLAW_SPEED;
        else if (this.gamepad1.left_bumper) this.clawOffset -= RobotTeleopTank_Iterative.CLAW_SPEED;

        // Move both servos to new position.  Assume servos are mirror image of each other.
	    this.clawOffset = Range.clip(this.clawOffset, -0.5, 0.5);
	    this.leftClaw.setPosition(RobotTeleopTank_Iterative.MID_SERVO + this.clawOffset);
	    this.rightClaw.setPosition(RobotTeleopTank_Iterative.MID_SERVO - this.clawOffset);

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (this.gamepad1.y) this.leftArm.setPower(RobotTeleopTank_Iterative.ARM_UP_POWER);
        else if (this.gamepad1.a) this.leftArm.setPower(RobotTeleopTank_Iterative.ARM_DOWN_POWER);
        else this.leftArm.setPower(0.0);

        // Send telemetry message to signify robot running;
	    this.telemetry.addData("claw",  "Offset = %.2f", this.clawOffset);
	    this.telemetry.addData("left",  "%.2f", left);
	    this.telemetry.addData("right", "%.2f", right);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
