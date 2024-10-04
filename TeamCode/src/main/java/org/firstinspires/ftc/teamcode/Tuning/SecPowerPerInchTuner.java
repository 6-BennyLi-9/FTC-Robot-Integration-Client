package org.firstinspires.ftc.teamcode.Tuning;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyButtonType;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyMapSettingType;
import org.firstinspires.ftc.teamcode.Hardwares.Integration.Gamepad.KeyTag;
import org.firstinspires.ftc.teamcode.Templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.Utils.Timer;

@TeleOp(name = "SecPowerPerInchTuner",group = "tune")
public class SecPowerPerInchTuner extends TuningProgramTemplate {
	@Override
	public void whileActivating() {
		if(robot.gamepad.getButtonRunAble(KeyTag.TuningButton1)){
			robot.motors.simpleMotorPowerController(0,1,0);
			Actions.runBlocking(new ParallelAction(
					new Action() {
						final Timer timer=new Timer();
						boolean initialized=false;

						@Override
						public boolean run(@NonNull TelemetryPacket telemetryPacket) {
							robot.motors.updateDriveOptions();
							if(!initialized){
								timer.pushTimeTag("drove");
								initialized=true;
								return true;
							}
							return timer.getTimeTag("drove")<1000;
						}
					}
			));
		}
	}

	@Override
	public void whenInit() {
		robot.gamepad.keyMap.loadButtonContent(KeyTag.TuningButton1, KeyButtonType.A, KeyMapSettingType.RunWhenButtonPressed);
		robot.registerGamepad(gamepad1,gamepad2);

		robot.client.addLine("按下A键后，机器会开始向前行驶1s");
		robot.client.addLine("⚠⚠⚠当心机器伤人⚠⚠⚠");
	}
}
