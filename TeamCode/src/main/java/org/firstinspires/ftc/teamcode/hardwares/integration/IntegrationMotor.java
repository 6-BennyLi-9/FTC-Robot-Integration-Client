package org.firstinspires.ftc.teamcode.hardwares.integration;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

/**
 * 集成化的单个电机控制类
 * <p>
 * 注意要及时 update() ，否则参数不会下达到电机
 */
public class IntegrationMotor extends IntegrationDevice{
	private boolean PID_ENABLED;

	public final DcMotorEx motor;
	private final PidProcessor pidProcessor;
	private double power;

	public IntegrationMotor(@NonNull final DcMotorEx motor, @NonNull final HardwareDeviceTypes deviceType, final PidProcessor pidProcessor){
		super(deviceType.deviceName);
		this.motor= motor;
		this.pidProcessor=pidProcessor;
	}

	@UserRequirementFunctions
	public void initPID(final int ParamID){
		if(this.PID_ENABLED){
			this.pidTag = getClass().getName() + "-" + this.motor.getDeviceName();
			this.pidProcessor.loadContent(new PidContent(this.pidTag,ParamID));
		}
	}

	public void setPower(double power){
		power= Mathematics.intervalClip(power, - 1, 1);

		this.power = power;
		this.updated =false;

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			this.update();
		}
	}

	@UserRequirementFunctions
	public void reverse(){
		this.motor.setDirection(REVERSE == motor.getDirection() ? FORWARD : REVERSE);
	}
	@UserRequirementFunctions
	public boolean isReversed(){
		return REVERSE == motor.getDirection();
	}

	@Override
	public void update() {
		if(this.updated)return;
		this.updated =true;

//		Global.client.changeData("in box power",power);

		if(this.PID_ENABLED && null != pidProcessor){
			//警告：如果沒有匹配的 PID Params 電極將無法轉動
//			Global.client.changeData(motor.getDeviceName(),"Use PID Running");
			this.pidProcessor.registerInaccuracies(this.pidTag, this.power - this.motor.getPower());
			this.pidProcessor.ModifyPidByTag(this.pidTag);
			this.motor.setPower(this.motor.getPower() + this.pidProcessor.getFulfillment(this.pidTag));
		}else{
//			Global.client.changeData(motor.getDeviceName(),"No PID Running");
			this.motor.setPower(this.power);
		}
//		timer.pushTimeTag("LastUpdateTime");
	}

	@Override
	@ExtractedInterfaces
	public double getPosition() {
		return this.motor.getCurrentPosition();
	}

	@Override
	@ExtractedInterfaces
	public double getPower() {
		return this.motor.getPower();
	}

	@UserRequirementFunctions
	public void ConfigPidEnable(final boolean val) {
		this.PID_ENABLED = val;
	}
}
