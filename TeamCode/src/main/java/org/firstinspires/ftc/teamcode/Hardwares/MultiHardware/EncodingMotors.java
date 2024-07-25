package org.firstinspires.ftc.teamcode.Hardwares.MultiHardware;

import org.firstinspires.ftc.teamcode.Hardwares.Single.EncodingMotor;

import java.util.List;

public class EncodingMotors {
	List<EncodingMotor> motors;
	protected int targetPosition,bufVal=20;
	protected double power=0.6f;
	boolean paused;

	public void setTargetPosition(int encoder){
		targetPosition=encoder;
	}

	/**
	 * @param power 不可以为0,如果想要中止电机，更改paused的值
	 */
	public void setPower(double power){
		if(power==0)return;
		this.power=Math.abs(power);
		power=Math.min(1,power);
	}

	public void update(){
		for(EncodingMotor mt:motors){
			mt.setPower(paused ? 0 : this.power);
			mt.update();
		}
	}
}
