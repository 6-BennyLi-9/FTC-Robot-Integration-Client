package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

@TeleOp(name = "SecPowerPerInchTuner",group = Params.Configs.TuningAndTuneOpModesGroup)
public class SecPowerPerInchTuner extends TuningProgramTemplate {
	protected double getAxialBufPower(){
		return 1;
	}
	protected double getLateralBufPower(){
		return 1;
	}

	private double axialOnBotPower,lateralOnBotPower;

	public double getAxialOnBotBufPower(){return axialOnBotPower;}
	public double getLateralOnBotBufPower(){return lateralOnBotPower;}

	@Override
	public void whileActivating() {
		boolean runAction = false;

		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton1)) {//前
			runAction =true;
			axialOnBotPower=getAxialBufPower();
			lateralOnBotPower=0;
		} else if (robot.gamepad.getButtonRunAble(KeyTag.TuningButton2)) {//后
			runAction =true;
			axialOnBotPower=-getAxialBufPower();
			lateralOnBotPower=0;
		} else if (robot.gamepad.getButtonRunAble(KeyTag.TuningButton3)) {//左
			runAction =true;
			axialOnBotPower=0;
			lateralOnBotPower=-getLateralBufPower();
		} else if (robot.gamepad.getButtonRunAble(KeyTag.TuningButton4)) {//右
			runAction =true;
			axialOnBotPower=0;
			lateralOnBotPower=getLateralBufPower();
		}
		if(runAction){
			Actions.runBlocking(new ParallelAction(
					new Action() {
						final Timer timer=new Timer();
						boolean initialized=false;

						@Override
						public boolean run(@NonNull TelemetryPacket telemetryPacket) {
							robot.motors.simpleMotorPowerController(getLateralOnBotBufPower(), getAxialOnBotBufPower(),0);
							robot.motors.updateDriveOptions();
							robot.motors.clearDriveOptions();
							if(!initialized){
								timer.pushTimeTag("drove");
								initialized=true;
								return true;
							}

							robot.client.changeData("Current Delta Time Mills", Timer.getCurrentTime() - timer.getTimeTag("drove"));
							robot.motors.showPowers();

							return Timer.getCurrentTime() - timer.getTimeTag("drove") < 1000;
						}
					}
			));
		}else{
			Actions.runBlocking(telemetryPacket -> {
				robot.motors.clearDriveOptions();
				robot.motors.updateDriveOptions();
				return false;
			});
		}
	}

	@Override
	public void whenInit() {
		registerGamePad();
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.Y, KeyMapSettingType.RunWhenButtonHold);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton2, KeyButtonType.A, KeyMapSettingType.RunWhenButtonHold);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton3, KeyButtonType.X, KeyMapSettingType.RunWhenButtonHold);
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton4, KeyButtonType.B, KeyMapSettingType.RunWhenButtonHold);

		// Y
		//X B
		// A

		robot.addLine("按下 Y 键后，机器会开始向前行驶1s");
		robot.addLine("按下 A 键后，机器会开始向后行驶1s");
		robot.addLine("按下 X 键后，机器会开始向左行驶1s");
		robot.addLine("按下 B 键后，机器会开始向右行驶1s");

		robot.addLine("⚠⚠⚠当心机器伤人⚠⚠⚠");
	}
}
