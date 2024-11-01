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
		this.leftFront = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.LeftFront);
		this.leftRear = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.LeftRear);
		this.rightFront = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.RightFront);
		this.rightRear = (IntegrationMotor) Global.integrationHardwareMap.getDevice(HardwareDeviceTypes.RightRear);
		this.client.addLine("按A鍵以REVERSE LeftRear");
		this.client.addLine("按B鍵以REVERSE RightRear");
		this.client.addLine("按X鍵以REVERSE LeftFront");
		this.client.addLine("按Y鍵以REVERSE LeftRear");
		this.registerGamePad();
		this.robot.gamepad.keyMap=new KeyMap();
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton2, KeyButtonType.B, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton3, KeyButtonType.X, KeyMapSettingType.RunWhenButtonPressed)
				.loadButtonContent(KeyTag.TuningButton4, KeyButtonType.Y, KeyMapSettingType.RunWhenButtonPressed);

//		Global.integrationHardwareMap.printSettings();
	}

	@Override
	public void whileActivating() {
		if(this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton1)){
			this.leftRear.reverse();
		}
		if(this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton2)){
			this.rightRear.reverse();
		}
		if(this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton3)){
			this.leftFront.reverse();
		}
		if(this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton4)){
			this.rightFront.reverse();
		}

		this.leftFront.setPower(0.01);
		this.leftRear.setPower(0.01);
		this.rightFront.setPower(0.01);
		this.rightRear.setPower(0.01);

		this.leftFront.update();
		this.leftRear.update();
		this.rightFront.update();
		this.rightRear.update();

		this.client.changeData("LeftFront Motor Direction", this.leftFront.isReversed());
		this.client.changeData("LeftRear Motor Direction", this.leftRear.isReversed());
		this.client.changeData("RightFront Motor Direction", this.rightFront.isReversed());
		this.client.changeData("RightRear Motor Direction", this.rightRear.isReversed());

		this.client.changeData("LeftFront Motor Power", this.leftFront.getPower());
		this.client.changeData("LeftRear Motor Power", this.leftRear.getPower());
		this.client.changeData("RightFront Motor Power", this.rightFront.getPower());
		this.client.changeData("RightRear Motor Power", this.rightRear.getPower());
	}
}
