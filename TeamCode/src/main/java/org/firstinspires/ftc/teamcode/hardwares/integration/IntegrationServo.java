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
	public boolean smoothMode;

	private double targetPose,targetVelocity;
	public final Servo servo;

	@UserRequirementFunctions
	public IntegrationServo(@NonNull final Servo servo, @NonNull final HardwareDeviceTypes deviceType,
	                        final double positionPerRadian, final double speed, final double basePose){
		super(deviceType.deviceName);
		this.servo= servo;
		this.positionPerRadian=positionPerRadian;
		this.speed=speed;
		this.basePose=basePose;
		this.lazyMode =false;
	}
	@UserRequirementFunctions
	public IntegrationServo(@NonNull final Servo servo, @NonNull final HardwareDeviceTypes deviceType){
		super(deviceType.deviceName);
		this.servo= servo;
		this.positionPerRadian =0;
		this.speed =0;
		this.basePose =0;
		this.lazyMode =true;
	}

	@UserRequirementFunctions
	public void setTargetPose(final double targetPose) {
		if(this.smoothMode) this.smoothMode =false;
		this.targetPose = targetPose;
		this.updated =false;

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			this.update();
		}
	}
	@UserRequirementFunctions
	public void setTargetPoseInTime(final double targetPose, final double TimeMills){
		this.smoothMode =true;
		this.targetVelocity = (targetPose - this.servo.getPosition()) / TimeMills;
		if(! this.lazyMode && this.targetVelocity > this.speed){
			this.targetVelocity = this.speed;
		}
		Log.w("W","建议提供 speed");
		Integrations.timer.pushObjectionTimeTag("InTimeStartTag",TimeMills);

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			this.update();
		}
	}

	@ExtractedInterfaces
	public void reset(){
		this.setTargetPose(this.basePose);
	}

	@Override
	public void update() {
		if(this.smoothMode){
			if((double) Integrations.timer.getTimeTagObjection("InTimeStartTag") + Integrations.timer.getTimeTag("InTimeStartTag") > Timer.getCurrentTime()){
				if(0.05f < targetPose - servo.getPosition()){
					this.servo.setPosition(this.targetPose);
				}else{
					this.smoothMode =false;
					//done
				}
			}else{
				final double delta = this.targetVelocity * (Timer.getCurrentTime() - Integrations.timer.getTimeTag("LastUpdateTime"));
				this.servo.setPosition(this.servo.getPosition() + delta);
			}
		}else {
			this.servo.setPosition(this.targetPose);
		}
		Integrations.timer.pushTimeTag("LastUpdateTime");
		this.updated =true;
	}

	public boolean inPlace(){
		return AllowErrorPosition > Math.abs(servo.getPosition() - targetPose);
	}

	@Override
	@ExtractedInterfaces
	public double getPosition() {
		return this.servo.getPosition();
	}
}
