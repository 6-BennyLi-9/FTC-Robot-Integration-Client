package org.firstinspires.ftc.teamcode.Samples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Templates.TeleopProgramTemplate;

@TeleOp(name = "ManualCodeSample",group = "samples")
public class ManualCodeSample extends TeleopProgramTemplate {
	@Override
	public void whenInit() {
		robot.registerGamepad(gamepad1,gamepad2);
	}

	@Override
	public void whileActivating() {
		robot.operateThroughGamePad();
		robot.update();
	}

}
