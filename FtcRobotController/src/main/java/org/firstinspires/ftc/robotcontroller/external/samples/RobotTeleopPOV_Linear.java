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
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/*
 * This OpMode executes a POV Game style Teleop for a direct drive robot
 * The code is structured as a LinearOpMode
 *
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the arm using the Gamepad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Robot: Teleop POV", group="Robot")
@Disabled
public class RobotTeleopPOV_Linear extends LinearOpMode {

    /* Declare OpMode members. */
    public DcMotor  leftDrive;
    public DcMotor  rightDrive;
    public DcMotor  leftArm;
    public Servo    leftClaw;
    public Servo    rightClaw;

    double clawOffset;

    public static final double MID_SERVO   =  0.5 ;
    public static final double CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    @Override
    public void runOpMode() {
        double left;
        double right;
        double drive;
        double turn;
        double max;

        // Define and Initialize Motors
	    this.leftDrive = this.hardwareMap.get(DcMotor.class, "left_drive");
	    this.rightDrive = this.hardwareMap.get(DcMotor.class, "right_drive");
	    this.leftArm = this.hardwareMap.get(DcMotor.class, "left_arm");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
	    this.leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
	    this.rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        // If there are encoders connected, switch to RUN_USING_ENCODER mode for greater accuracy
        // leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
	    this.leftClaw = this.hardwareMap.get(Servo.class, "left_hand");
	    this.rightClaw = this.hardwareMap.get(Servo.class, "right_hand");
	    this.leftClaw.setPosition(RobotTeleopPOV_Linear.MID_SERVO);
	    this.rightClaw.setPosition(RobotTeleopPOV_Linear.MID_SERVO);

        // Send telemetry message to signify robot waiting;
	    this.telemetry.addData(">", "Robot Ready.  Press START.");    //
	    this.telemetry.update();

        // Wait for the game to start (driver presses START)
	    this.waitForStart();

        // run until the end of the match (driver presses STOP)
        while (this.opModeIsActive()) {

            // Run wheels in POV mode (note: The joystick goes negative when pushed forward, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            // This way it's also easy to just drive straight, or just turn.
            drive = - this.gamepad1.left_stick_y;
            turn  = this.gamepad1.right_stick_x;

            // Combine drive and turn for blended motion.
            left  = drive + turn;
            right = drive - turn;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (1.0 < max)
            {
                left /= max;
                right /= max;
            }

            // Output the safe vales to the motor drives.
	        this.leftDrive.setPower(left);
	        this.rightDrive.setPower(right);

            // Use gamepad left & right Bumpers to open and close the claw
            if (this.gamepad1.right_bumper) this.clawOffset += RobotTeleopPOV_Linear.CLAW_SPEED;
            else if (this.gamepad1.left_bumper) this.clawOffset -= RobotTeleopPOV_Linear.CLAW_SPEED;

            // Move both servos to new position.  Assume servos are mirror image of each other.
	        this.clawOffset = Range.clip(this.clawOffset, -0.5, 0.5);
	        this.leftClaw.setPosition(RobotTeleopPOV_Linear.MID_SERVO + this.clawOffset);
	        this.rightClaw.setPosition(RobotTeleopPOV_Linear.MID_SERVO - this.clawOffset);

            // Use gamepad buttons to move arm up (Y) and down (A)
            if (this.gamepad1.y) this.leftArm.setPower(RobotTeleopPOV_Linear.ARM_UP_POWER);
            else if (this.gamepad1.a) this.leftArm.setPower(RobotTeleopPOV_Linear.ARM_DOWN_POWER);
            else this.leftArm.setPower(0.0);

            // Send telemetry message to signify robot running;
	        this.telemetry.addData("claw",  "Offset = %.2f", this.clawOffset);
	        this.telemetry.addData("left",  "%.2f", left);
	        this.telemetry.addData("right", "%.2f", right);
	        this.telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
	        this.sleep(50);
        }
    }
}
