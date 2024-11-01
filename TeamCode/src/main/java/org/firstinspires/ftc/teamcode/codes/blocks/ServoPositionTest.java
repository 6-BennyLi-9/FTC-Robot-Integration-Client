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

		this.leftIntake = this.hardwareMap.get(Servo.class, "leftIntake");
		this.rightIntake = this.hardwareMap.get(Servo.class, "rightIntake");

		// Put initialization blocks here.
		this.waitForStart();
		pose = 0.5;
		if (this.opModeIsActive()) {
			while (this.opModeIsActive()) {
				// Put loop blocks here.
				this.telemetry.update();
				if (this.gamepad1.a) {
					pose -= 0.05;
				}
				if (this.gamepad1.y) {
					pose += 0.05;
				}
				this.leftIntake.setPosition(pose);
				this.rightIntake.setPosition(pose);
				this.sleep(100);
				this.telemetry.addData("pose", pose);
			}
		}
	}
}
