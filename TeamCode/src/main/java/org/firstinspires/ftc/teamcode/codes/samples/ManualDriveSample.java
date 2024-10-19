package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TeleopProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

@TeleOp(name = "ManualDriveSample",group = Params.Configs.SampleOpModesGroup)
public class ManualDriveSample extends TeleopProgramTemplate {
	@Override
	public void whenInit() {
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunForward, KeyRodType.LeftStickY, KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisRunStrafe, KeyRodType.LeftStickX, KeyMapSettingType.PullRod);
		robot.gamepad.keyMap.loadRodContent(KeyTag.ChassisTurn, KeyRodType.RightStickX, KeyMapSettingType.PullRod);
	}

	@Override
	public void whileActivating() {
		robot.chassis.motors.simpleMotorPowerController(
				robot.gamepad.getRodState(KeyTag.ChassisRunStrafe)*0.8,
				robot.gamepad.getRodState(KeyTag.ChassisRunForward)*0.8,
				robot.gamepad.getRodState(KeyTag.ChassisTurn)*0.8
		);

		robot.chassis.motors.updateDriveOptions();
		robot.chassis.motors.clearDriveOptions();
	}
}
