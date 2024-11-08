package org.firstinspires.ftc.teamcode.actions.ric;

import org.firstinspires.ftc.teamcode.actions.Action;
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
	public boolean run() {
		if(! this.optionPushed){
			this.optionPushed =true;
			this.motor.setTargetPosition(this.targetPose);
		}
		this.motor.update();
		return ! this.motor.inPlace();
	}
}
