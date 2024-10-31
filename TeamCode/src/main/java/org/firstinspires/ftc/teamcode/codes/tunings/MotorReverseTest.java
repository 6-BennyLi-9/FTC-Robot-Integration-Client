package org.firstinspires.ftc.teamcode.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Global;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.keymap.KeyMap;

@TeleOp(name = "MotorReverseTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class MotorReverseTest extends TuningProgramTemplate {
	IntegrationMotor leftFront, leftRear, rightFront, rightRear;

	@Override
	public void whenInit() {
		leftFront = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.LeftFront);
		leftRear = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.LeftRear);
		rightFront = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.RightFront);
		rightRear = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.RightRear);
		client.addLine("按A鍵以REVERSE LeftRear");
		client.addLine("按B鍵以REVERSE RightRear");
		client.addLine("按X鍵以REVERSE LeftFront");
		client.addLine("按Y鍵以REVERSE LeftRear");
		registerGamePad();
		robot.gamepad.keyMap=new KeyMap();
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton2, KeyButtonType.B, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton3, KeyButtonType.X, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton4, KeyButtonType.Y, KeyMapSettingType.RunWhenButtonPressed);

//		Global.integrationHardwareMap.printSettings();
	}

	@Override
	public void whileActivating() {
		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton1)){
			leftRear.reverse();
		}
		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton2)){
			rightRear.reverse();
		}
		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton3)){
			leftFront.reverse();
		}
		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton4)){
			rightFront.reverse();
		}

		leftFront.setPower(0.01);
		leftRear.setPower(0.01);
		rightFront.setPower(0.01);
		rightRear.setPower(0.01);

		leftFront.update();
		leftRear.update();
		rightFront.update();
		rightRear.update();

		client.changeData("LeftFront Motor Direction",leftFront.isReversed());
		client.changeData("LeftRear Motor Direction",leftRear.isReversed());
		client.changeData("RightFront Motor Direction",rightFront.isReversed());
		client.changeData("RightRear Motor Direction",rightRear.isReversed());

		client.changeData("LeftFront Motor Power",leftFront.getPower());
		client.changeData("LeftRear Motor Power",leftRear.getPower());
		client.changeData("RightFront Motor Power",rightFront.getPower());
		client.changeData("RightRear Motor Power",rightRear.getPower());
	}
}
