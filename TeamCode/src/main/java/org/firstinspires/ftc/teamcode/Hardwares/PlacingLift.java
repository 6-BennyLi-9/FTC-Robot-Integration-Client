package org.firstinspires.ftc.teamcode.Hardwares;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Hardwares.Single.SimpleMotor;

public class PlacingLift {
	SimpleMotor lift;
	protected int targetPosition,bufVal=20;
	protected double power=0.6f;
	private class runner implements Action{
		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			lift.paused=false;
			lift.bufVal=bufVal;
			lift.setTarget(targetPosition,power);
			if (lift.done()) {
				lift.paused=true;
				return false;
			}
			else {
				return true;
			}
		}
	}

	public void setTarget(int targetPosition,int power) {
		this.targetPosition = targetPosition;
		this.power=power;
	}

	/**
	 * 不执行操作
	 */
	public void update(){
		lift.setTarget(targetPosition,power);
	}

	/**
	 * 自动等待至调整结束
	 */
	public Action placingAction(){
		return new runner();
	}
}
