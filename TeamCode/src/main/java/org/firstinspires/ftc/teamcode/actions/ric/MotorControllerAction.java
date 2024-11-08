package org.firstinspires.ftc.teamcode.actions.ric;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.hardwares.integration.PositionalIntegrationMotor;

public class MotorControllerAction implements Action {
	private final PositionalIntegrationMotor motor;
	private final int targetPose;

	public MotorControllerAction(final PositionalIntegrationMotor motor, final int pose){
		this.motor=motor;
		this.targetPose =pose;
	}

	private boolean optionPushed;

	@Override
	public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
		if(! this.optionPushed){
			this.optionPushed =true;
			this.motor.setTargetPosition(this.targetPose);
		}
		this.motor.update();
		return ! this.motor.inPlace();
	}
}
