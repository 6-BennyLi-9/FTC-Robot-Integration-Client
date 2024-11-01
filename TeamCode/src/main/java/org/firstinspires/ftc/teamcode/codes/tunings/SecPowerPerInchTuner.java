package org.firstinspires.ftc.teamcode.codes.tunings;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyButtonType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.hardwares.integration.gamepads.KeyTag;
import org.firstinspires.ftc.teamcode.utils.Timer;

@TeleOp(name = "SecPowerPerInchTuner",group = Params.Configs.TuningAndTuneOpModesGroup)
public class SecPowerPerInchTuner extends TuningProgramTemplate {
	protected double getAxialBufPower(){
		return 1;
	}
	protected double getLateralBufPower(){
		return 1;
	}

	private double axialOnBotPower,lateralOnBotPower;

	public double getAxialOnBotBufPower(){return this.axialOnBotPower;}
	public double getLateralOnBotBufPower(){return this.lateralOnBotPower;}

	@Override
	public void whileActivating() {
		boolean runAction = false;

		if(this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton1)) {//前
			runAction =true;
			this.axialOnBotPower = this.getAxialBufPower();
			this.lateralOnBotPower =0;
		} else if (this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton2)) {//后
			runAction =true;
			this.axialOnBotPower =- this.getAxialBufPower();
			this.lateralOnBotPower =0;
		} else if (this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton3)) {//左
			runAction =true;
			this.axialOnBotPower =0;
			this.lateralOnBotPower =- this.getLateralBufPower();
		} else if (this.robot.gamepad.getButtonRunAble(KeyTag.TuningButton4)) {//右
			runAction =true;
			this.axialOnBotPower =0;
			this.lateralOnBotPower = this.getLateralBufPower();
		}
		if(runAction){
			Actions.runBlocking(new ParallelAction(
					new Action() {
						final Timer timer=new Timer();
						boolean initialized;

						@Override
						public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
							SecPowerPerInchTuner.this.robot.motors.simpleMotorPowerController(SecPowerPerInchTuner.this.getLateralOnBotBufPower(), SecPowerPerInchTuner.this.getAxialOnBotBufPower(),0);
							SecPowerPerInchTuner.this.robot.motors.updateDriveOptions();
							SecPowerPerInchTuner.this.robot.motors.clearDriveOptions();
							if(! this.initialized){
								this.timer.pushTimeTag("drove");
								this.initialized =true;
								return true;
							}

							SecPowerPerInchTuner.this.robot.client.changeData("Current Delta Time Mills", Timer.getCurrentTime() - this.timer.getTimeTag("drove"));
							SecPowerPerInchTuner.this.robot.motors.showPowers();

							return 1000 > Timer.getCurrentTime() - timer.getTimeTag("drove");
						}
					}
			));
		}else{
			Actions.runBlocking(telemetryPacket -> {
				this.robot.motors.clearDriveOptions();
				this.robot.motors.updateDriveOptions();
				return false;
			});
		}
	}

	@Override
	public void whenInit() {
		this.registerGamePad();
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.Y, KeyMapSettingType.RunWhenButtonHold);
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton2, KeyButtonType.A, KeyMapSettingType.RunWhenButtonHold);
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton3, KeyButtonType.X, KeyMapSettingType.RunWhenButtonHold);
		this.robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton4, KeyButtonType.B, KeyMapSettingType.RunWhenButtonHold);

		// Y
		//X B
		// A

		this.robot.addLine("按下 Y 键后，机器会开始向前行驶1s");
		this.robot.addLine("按下 A 键后，机器会开始向后行驶1s");
		this.robot.addLine("按下 X 键后，机器会开始向左行驶1s");
		this.robot.addLine("按下 B 键后，机器会开始向右行驶1s");

		this.robot.addLine("⚠⚠⚠当心机器伤人⚠⚠⚠");
	}
}
