package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;

import java.util.Objects;

@TeleOp(name = "IntegrationGamepadTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class IntegrationGamepadTest extends TuningProgramTemplate {
	private IntegrationGamepad gamepad;
	private int count=0;

	@Override
	public void whileActivating() {
		robot.client.changeData("KBT(A)", Objects.requireNonNull(gamepad.keyMap.contents.get(KeyTag.TuningButton1)).tag.name());
		robot.client.changeData("KMS(A)", Objects.requireNonNull(gamepad.keyMap.contents.get(KeyTag.TuningButton1)).setting.name());
//		robot.client.changeData("A(current)",gamepad.gamepad1.A());
		robot.client.changeData("A(integration)",gamepad.getButtonRunAble(KeyTag.TuningButton1));

		if(gamepad.getButtonRunAble(KeyTag.TuningButton1)) ++count;

		robot.client.changeData("A(count)",count);
//		robot.client.changeData("A(integration-current)",gamepad.gamepad1.getButtonState(KeyButtonType.A,KeyMapSettingType.RunWhenButtonHold));
//		robot.client.changeData("A(integration-last)",gamepad.gamepad1.LastState.get(KeyButtonType.A));

		robot.client.changeData("LeftStickX(current)",gamepad.gamepad1.LeftStickX());
		robot.client.changeData("LeftStickX(integration)",gamepad.getRodState(KeyTag.TuningButton2));

		gamepad.showLst();
//		gamepad.showContentInfo();
	}

	@Override
	public void whenInit() {
		gamepad=new IntegrationGamepad(gamepad1,gamepad2);
		gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed,true);
		gamepad.keyMap.loadRodContent(KeyTag.TuningButton2, KeyRodType.LeftStickX,KeyMapSettingType.PullRod,true);
	}
}
