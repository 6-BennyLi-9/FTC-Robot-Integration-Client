package org.firstinspires.ftc.teamcode.codes.blocks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoPowerTest (Blocks to Java)")
public class ServoPowerTest extends LinearOpMode {

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
		leftIntake = hardwareMap.get(Servo.class, "leftIntake");
		rightIntake = hardwareMap.get(Servo.class, "rightIntake");

		// Put initialization blocks here.
		waitForStart();
		leftIntake.setDirection(Servo.Direction.FORWARD);
		leftIntake.setDirection(Servo.Direction.FORWARD);
		if (opModeIsActive()) {
			while (opModeIsActive()) {
				rightIntake.setPosition(0.5);
				telemetry.addData("left", leftIntake.getPosition());
				telemetry.addData("right", rightIntake.getPosition());
				telemetry.update();
			}
		}
	}
}
