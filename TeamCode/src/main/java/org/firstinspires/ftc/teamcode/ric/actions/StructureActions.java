package org.firstinspires.ftc.teamcode.ric.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationServo;
import org.firstinspires.ftc.teamcode.ric.hardwares.Structure;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.UserRequirementFunctions;

public class StructureActions {
	public Structure controller;

	public StructureActions(Structure controller){
		this.controller=controller;
	}

	protected class OpenFrontClip implements Action {
		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			controller.openFrontClip();
			controller.servos.update();
			return controller.servos.inPlace();
		}
	}
	protected class CloseFrontClip implements Action {
		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			controller.closeFrontClip();
			controller.servos.update();
			return controller.servos.inPlace();
		}
	}
	protected class OpenRearClip implements Action {
		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			controller.openRearClip();
			controller.servos.update();
			return controller.servos.inPlace();
		}
	}
	protected class CloseRearClip implements Action {
		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			controller.closeRearClip();
			controller.servos.update();
			return controller.servos.inPlace();
		}
	}

	protected static class ServoToPlace implements Action{
		public IntegrationServo servo;
		public final double pose,time;
		public ServoToPlace(@NonNull IntegrationServo servo, double pose, double time){
			this.servo=servo;
			this.pose=pose;
			this.time=time;
			servo.setTargetPoseInTime(pose,time);
		}

		@Override
		public boolean run(@NonNull TelemetryPacket telemetryPacket) {
			servo.update();
			return servo.smoothMode;
		}
	}

	@UserRequirementFunctions
	public Action openFrontClip(){return new OpenFrontClip();}
	@UserRequirementFunctions
	public Action openRearClip(){return new OpenRearClip();}
	@UserRequirementFunctions
	public Action closeFrontClip(){return new CloseFrontClip();}
	@UserRequirementFunctions
	public Action closeRearClip(){return new CloseRearClip();}


	@UserRequirementFunctions
	public Action openClips(){return new ParallelAction(openFrontClip(),openRearClip());}
	@UserRequirementFunctions
	public Action closeClips(){return new ParallelAction(closeFrontClip(),closeRearClip());}

	@UserRequirementFunctions
	public Action servoToPlace(@NonNull IntegrationServo servo, double pose, double time){return new ServoToPlace(servo, pose, time);}
}
