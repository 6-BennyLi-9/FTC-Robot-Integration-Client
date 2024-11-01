package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationGamepad;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyRodType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;

import java.util.Objects;

@TeleOp(name = "IntegrationGamepadTest",group = Params.Configs.TuningAndTuneOpModesGroup)
public class IntegrationGamepadTest extends TuningProgramTemplate {
	private IntegrationGamepad gamepad;
	private int count;

	@Override
	public void whileActivating() {
		this.robot.client.changeData("KBT(A)", Objects.requireNonNull(this.gamepad.keyMap.contents.get(KeyTag.TuningButton1)).tag.name());
		this.robot.client.changeData("KMS(A)", Objects.requireNonNull(this.gamepad.keyMap.contents.get(KeyTag.TuningButton1)).setting.name());
//		robot.client.changeData("A(current)",gamepad.gamepad1.A());
		this.robot.client.changeData("A(integration)", this.gamepad.getButtonRunAble(KeyTag.TuningButton1));

		if(this.gamepad.getButtonRunAble(KeyTag.TuningButton1)) ++ this.count;

		this.robot.client.changeData("A(count)", this.count);
//		robot.client.changeData("A(integration-current)",gamepad.gamepad1.getButtonState(KeyButtonType.A,KeyMapSettingType.RunWhenButtonHold));
//		robot.client.changeData("A(integration-last)",gamepad.gamepad1.LastState.get(KeyButtonType.A));

		this.robot.client.changeData("LeftStickX(current)", this.gamepad.gamepad1.LeftStickX());
		this.robot.client.changeData("LeftStickX(integration)", this.gamepad.getRodState(KeyTag.TuningButton2));

		this.gamepad.showLst();
//		gamepad.showContentInfo();
	}

	@Override
	public void whenInit() {
		this.gamepad =new IntegrationGamepad(this.gamepad1, this.gamepad2);
		this.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed,true);
		this.gamepad.keyMap.loadRodContent(KeyTag.TuningButton2, KeyRodType.LeftStickX,KeyMapSettingType.PullRod,true);
	}
}
