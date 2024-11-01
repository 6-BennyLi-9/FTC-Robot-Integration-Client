package org.firstinspires.ftc.teamcode.codes.blocks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoPositionTest (Blocks to Java)")
public class ServoPositionTest extends LinearOpMode {

	public Servo leftIntake;
	public Servo rightIntake;

	/**
	 * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
	 * Comment Blocks show where to place Initialization code (runs once, after touching the
	 * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
	 * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
	 * Stopped).
	 */
	@Override
	public void runOpMode() {
		double pose;

		leftIntake = hardwareMap.get(Servo.class, "leftIntake");
		rightIntake = hardwareMap.get(Servo.class, "rightIntake");

		// Put initialization blocks here.
		waitForStart();
		pose = 0.5;
		if (opModeIsActive()) {
			while (opModeIsActive()) {
				// Put loop blocks here.
				telemetry.update();
				if (gamepad1.a) {
					pose -= 0.05;
				}
				if (gamepad1.y) {
					pose += 0.05;
				}
				leftIntake.setPosition(pose);
				rightIntake.setPosition(pose);
				sleep(100);
				telemetry.addData("pose", pose);
			}
		}
	}
}
