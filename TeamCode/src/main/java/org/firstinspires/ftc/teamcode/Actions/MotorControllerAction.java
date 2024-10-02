package org.firstinspires.ftc.teamcode.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Hardwares.Integration.PositionalIntegrationMotor;

public class MotorControllerAction implements Action {
	private final PositionalIntegrationMotor motor;
	private final int targetPose;

	public MotorControllerAction(PositionalIntegrationMotor motor,int pose){
		this.motor=motor;
		targetPose=pose;
	}

	private boolean optionPushed = false;

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if(!optionPushed){
			optionPushed=true;
			motor.setTargetPosition(targetPose);
		}
		motor.update();
		return !motor.inPlace();
	}
}
