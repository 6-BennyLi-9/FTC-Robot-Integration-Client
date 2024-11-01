package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TeleopProgramTemplate;

@TeleOp(name = "ManualCodeSample",group = Params.Configs.SampleOpModesGroup)
@Disabled
public class ManualCodeSample extends TeleopProgramTemplate {
	@Override
	public void whenInit() {
		this.robot.registerGamepad(this.gamepad1, this.gamepad2);
	}

	@Override
	public void whileActivating() {
		this.robot.operateThroughGamePad();
		this.robot.update();
	}

}
