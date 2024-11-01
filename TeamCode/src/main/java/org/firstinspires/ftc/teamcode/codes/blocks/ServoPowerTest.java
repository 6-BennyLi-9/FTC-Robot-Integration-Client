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
		this.leftIntake = this.hardwareMap.get(Servo.class, "leftIntake");
		this.rightIntake = this.hardwareMap.get(Servo.class, "rightIntake");

		// Put initialization blocks here.
		this.waitForStart();
		this.leftIntake.setDirection(Servo.Direction.FORWARD);
		this.leftIntake.setDirection(Servo.Direction.FORWARD);
		if (this.opModeIsActive()) {
			while (this.opModeIsActive()) {
				this.rightIntake.setPosition(0.5);
				this.telemetry.addData("left", this.leftIntake.getPosition());
				this.telemetry.addData("right", this.rightIntake.getPosition());
				this.telemetry.update();
			}
		}
	}
}
