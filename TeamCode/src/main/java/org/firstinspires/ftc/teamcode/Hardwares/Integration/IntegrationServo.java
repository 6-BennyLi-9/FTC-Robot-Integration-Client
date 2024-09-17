package org.firstinspires.ftc.teamcode.Hardwares.Integration;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardwares.Namespace.HardwareDevices;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UserRequirementFunctions;

public class IntegrationServo extends IntegrationDevice{
	public final double positionPerRadian,speed,basePose;
	private final boolean lazyMode;
	private boolean smoothMode=false;

	private double targetPose,targetVelocity;
	public final Servo servo;

	@UserRequirementFunctions
	public IntegrationServo(@NonNull Servo servo, @NonNull HardwareDevices deviceType,
	                        double positionPerRadian, double speed, double basePose){
		super(deviceType.deviceName);
		this.servo= servo;
		this.positionPerRadian=positionPerRadian;
		this.speed=speed;
		this.basePose=basePose;
		lazyMode=false;
	}
	@UserRequirementFunctions
	public IntegrationServo(@NonNull Servo servo, @NonNull HardwareDevices deviceType){
		super(deviceType.deviceName);
		this.servo= servo;
		positionPerRadian=0;
		speed=0;
		basePose=0;
		lazyMode=true;
	}

	@UserRequirementFunctions
	public void setTargetPose(double targetPose) {
		if(smoothMode) smoothMode=false;
		this.targetPose = targetPose;
		updated=false;
	}
	@UserRequirementFunctions
	public void setTargetPoseInTime(double targetPose,double TimeMills){
		smoothMode=true;
		targetVelocity=(targetPose-servo.getPosition())/TimeMills;
		timer.pushObjectionTimeTag("InTimeStartTag",TimeMills);
	}

	@Override
	public void update() {
		if(smoothMode){
			if((double) timer.getTimeTagObjection("InTimeStartTag")+timer.getTimeTag("InTimeStartTag")>timer.getCurrentTime()){
				if(targetPose-servo.getPosition()>0.05f){
					servo.setPosition(targetPose);
				}else{
					smoothMode=false;
					//done
				}
			}else{
				double delta=targetVelocity*(timer.getCurrentTime()-timer.getTimeTag("LastUpdateTime"));
				servo.setPosition(servo.getPosition()+delta);
			}
		}else {
			servo.setPosition(targetPose);
		}
		timer.pushTimeTag("LastUpdateTime");
		updated=true;
	}

	@Override
	public double getPosition() {
		return servo.getPosition();
	}
}