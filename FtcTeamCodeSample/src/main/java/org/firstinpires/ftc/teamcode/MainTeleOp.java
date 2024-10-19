package org.firstinpires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.codes.templates.TeleopProgramTemplate;

/**
 * 可以将该类重命名为队伍编号
 */
@TeleOp(name = "RENAME WITH TEAM CODE",group = "main")
@Disabled
public class MainTeleOp extends TeleopProgramTemplate {
	@Override
	public void whileActivating() {
		robot.operateThroughGamePad();
		robot.update();
	}

	@Override
	public void whenInit() {
		robot.registerGamepad(gamepad1,gamepad2);
		robot.setParamsOverride(new TeamCodeParams());
		robot.setKeyMapController(new TeamCodeKeyMap());
	}
}
