package org.firstinspires.ftc.teamcode.hardwares.integration;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.HardwareDeviceTypes;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.PID.PidContent;
import org.firstinspires.ftc.teamcode.utils.PID.PidProcessor;
/**
 * 集成化的单个电机控制类
 * <p>
 * 注意要及时 update() ，否则参数不会下达到电机
 * <p>
 * 使用 PID 或官方的控制是电机能到达指定编码值
 *
 * @see IntegrationMotor
 */
public class PositionalIntegrationMotor extends IntegrationDevice{
	private final static double AllowErrorPosition=15;

	private boolean PID_ENABLED =true;
	private boolean LAZY_MODE;

	public final DcMotorEx motor;
	private final PidProcessor pidProcessor;
	private double bufPower= 1.0f;
	private int targetPosition;

	public PositionalIntegrationMotor(@NonNull final DcMotorEx motor, @NonNull final HardwareDeviceTypes deviceType, final PidProcessor pidProcessor){
		super(deviceType.deviceName);
		this.motor=motor;
		this.pidProcessor=pidProcessor;
		motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
	}

	@UserRequirementFunctions
	public void initPID(final int ParamID){
		if(this.PID_ENABLED){
			this.pidTag = getClass().getName() + "-" + this.motor.getDeviceName();
			this.pidProcessor.loadContent(new PidContent(this.pidTag,ParamID));
		}
	}

	@UserRequirementFunctions
	public void setTargetPosition(final int position){
		this.targetPosition =position;
		this.updated =false;
	}
	@UserRequirementFunctions
	public void setBufPower(final double bufPower){
		this.bufPower=bufPower;
		this.updated =false;
	}

	@Override
	public void update() {
		final double kp = 1.0f;

		if(this.LAZY_MODE) {
			this.motor.setTargetPosition(this.targetPosition);
		}else{
			if (this.PID_ENABLED){
				this.pidProcessor.registerInaccuracies(this.pidTag, this.targetPosition - this.motor.getCurrentPosition());
				this.motor.setPower(this.pidProcessor.getFulfillment(this.pidTag));
			}else{
				this.motor.setPower(Mathematics.intervalClip(this.bufPower * (this.targetPosition - this.motor.getCurrentPosition()) / kp, - 1, 1));
			}
		}
		this.updated =true;

		if(Params.Configs.runUpdateWhenAnyNewOptionsAdded){
			this.update();
		}
	}

	@UserRequirementFunctions
	public boolean inPlace(){
		return AllowErrorPosition > Math.abs(motor.getCurrentPosition() - targetPosition);
	}

	@Override
	public double getPosition() {
		return this.motor.getCurrentPosition();
	}

	@UserRequirementFunctions
	public void setIsLazy(final boolean LAZY_MODE) {
		this.LAZY_MODE = LAZY_MODE;
	}

	@UserRequirementFunctions
	public void configPidEnable(final boolean val) {
		this.PID_ENABLED = val;
	}
}
