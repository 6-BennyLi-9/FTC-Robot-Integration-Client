package org.firstinspires.ftc.teamcode.ric.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.codes.templates.TeleopProgramTemplate;

@TeleOp(name = "ManualCodeSample2",group = "samples")
@Disabled
public class ManualCodeSample2 extends TeleopProgramTemplate {
	@Override
	public void whileActivating() {
		robot.operateThroughGamePad();
		robot.update();
	}

	@Override
	public void whenInit() {
		robot.registerGamepad(gamepad1,gamepad2);
//		robot.setKeyMapController(...);
//		robot.setParamsOverride(...);
	}
}
