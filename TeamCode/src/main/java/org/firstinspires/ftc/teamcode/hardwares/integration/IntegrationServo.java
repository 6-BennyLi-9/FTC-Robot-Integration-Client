package org.firstinspires.ftc.teamcode.hardwares.integration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 集成化的单个舵机控制类
 * <p>
 * 注意要及时 update() ，否则参数不会下达到舵机
 */
public class IntegrationServo extends IntegrationDevice{
	private final static double AllowErrorPosition=0.1;

	public final double positionPerRadian,speed,basePose;
	private final boolean lazyMode;
	public boolean smoothMode=false;

	private double targetPose,targetVelocity;
	public final Servo servo;

	@UserRequirementFunctions
	public IntegrationServo(@NonNull Servo servo, @NonNull HardwareDeviceTypes deviceType,
	                        double positionPerRadian, double speed, double basePose){
		super(deviceType.deviceName);
		this.servo= servo;
		this.positionPerRadian=positionPerRadian;
		this.speed=speed;
		this.basePose=basePose;
		lazyMode=false;
	}
	@UserRequirementFunctions
	public IntegrationServo(@NonNull Servo servo, @NonNull HardwareDeviceTypes deviceType){
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

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			update();
		}
	}
	@UserRequirementFunctions
	public void setTargetPoseInTime(double targetPose,double TimeMills){
		smoothMode=true;
		targetVelocity=(targetPose-servo.getPosition())/TimeMills;
		if(!lazyMode&&targetVelocity>speed){
			targetVelocity=speed;
		}
		Log.w("W","建议提供 speed");
		timer.pushObjectionTimeTag("InTimeStartTag",TimeMills);

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			update();
		}
	}

	@ExtractedInterfaces
	public void reset(){
		setTargetPose(basePose);
	}

	@Override
	public void update() {
		if(smoothMode){
			if((double) timer.getTimeTagObjection("InTimeStartTag")+timer.getTimeTag("InTimeStartTag") > Timer.getCurrentTime()){
				if(targetPose-servo.getPosition()>0.05f){
					servo.setPosition(targetPose);
				}else{
					smoothMode=false;
					//done
				}
			}else{
				double delta=targetVelocity*(Timer.getCurrentTime() - timer.getTimeTag("LastUpdateTime"));
				servo.setPosition(servo.getPosition()+delta);
			}
		}else {
			servo.setPosition(targetPose);
		}
		timer.pushTimeTag("LastUpdateTime");
		updated=true;
	}

	public boolean inPlace(){
		return Math.abs(servo.getPosition() - targetPose) < AllowErrorPosition;
	}

	@Override
	@ExtractedInterfaces
	public double getPosition() {
		return servo.getPosition();
	}
}
